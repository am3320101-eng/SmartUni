package org.example.smartunipro.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.smartunipro.dto.MaterialDto;
import org.example.smartunipro.dto.MaterialFilterDto;
import org.example.smartunipro.entity.Course;
import org.example.smartunipro.entity.Material;
import org.example.smartunipro.exception.CustomException;
import org.example.smartunipro.mapper.MaterialMapper;
import org.example.smartunipro.repository.CourseRepository;
import org.example.smartunipro.repository.FilterableRepository;
import org.example.smartunipro.repository.MaterialRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialServices extends FilterableService<Material, MaterialDto, MaterialFilterDto> {

    private static final List<String> SORTABLE_FIELDS = List.of("title");

    private final MaterialRepository materialRepository;
    private final CourseRepository   courseRepository;
    private final MaterialMapper     materialMapper;


    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    protected MaterialDto toDto(Material entity) {
        return materialMapper.toDto(entity);
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    public MaterialDto materialUpload(MultipartFile file, Long courseId, String title) {
        if (file == null || file.isEmpty()) {
            throw new CustomException("Uploaded file cannot be empty", HttpStatus.BAD_REQUEST);
        }


        Course course = resolveCourse(courseId);


        String pythonAiServerUrl = "https://your-app.up.railway.app/upload";
        String generatedPdfUrl = "";

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);


            ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };


            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileAsResource);
            body.add("courseId", courseId);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


            generatedPdfUrl = restTemplate.postForObject(pythonAiServerUrl, requestEntity, String.class);

        } catch (Exception e) {
            throw new CustomException("Failed to forward file to AI server: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        Material material = new Material();
        material.setTitle(title);
        material.setCourse(course);
        material.setPdfUrl(generatedPdfUrl);

        return materialMapper.toDto(materialRepository.save(material));
    }

    public List<MaterialDto> getAllMaterial() {
        return materialRepository.findAll()
                .stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
    }

    public MaterialDto getById(Long id) {
        return materialMapper.toDto(findOrThrow(id));
    }

    public MaterialDto update(Long id, MaterialDto dto) {
        Material material = findOrThrow(id);
        materialMapper.updateToEntity(dto, material);
        if (dto.getCourseId() != null) material.setCourse(resolveCourse(dto.getCourseId()));
        return materialMapper.toDto(materialRepository.save(material));
    }

    public void delete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new CustomException(
                    "Material not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        materialRepository.deleteById(id);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Material findOrThrow(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        "Material not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    private Course resolveCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        "Course not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    // ── FilterableService wiring ─────────────────────────────────────────────

    @Override
    protected List<String> sortableFields() {
        return SORTABLE_FIELDS;
    }

    @Override
    protected FilterableRepository<Material, ?> repository() {
        return materialRepository;
    }

    @Override
    protected Specification<Material> toSpec(MaterialFilterDto f) {
        SpecificationBuilder<Material> builder = SpecificationBuilder.<Material>builder()
                .like("title",      f.getTitle())
                .equal("course.id", f.getCourseId())
                .like("course.name", f.getCourseName());

        if (Boolean.TRUE.equals(f.getHasPdf())) {
            builder.isNotNull("pdfUrl");
        } else if (Boolean.FALSE.equals(f.getHasPdf())) {
            builder.isNull("pdfUrl");
        }

        if (Boolean.TRUE.equals(f.getHasVideo())) {
            builder.isNotNull("videoUrl");
        } else if (Boolean.FALSE.equals(f.getHasVideo())) {
            builder.isNull("videoUrl");
        }

        return builder.build();
    }
}