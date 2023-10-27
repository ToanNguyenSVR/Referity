package online.referity.repository;

import online.referity.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JobTitleRepository extends JpaRepository<JobTitle , UUID> {
    @Query("Select j From JobTitle j Where j.position like %?1% ")
    public List<JobTitle> seach(String key);

    JobTitle getById(UUID Id);
}
