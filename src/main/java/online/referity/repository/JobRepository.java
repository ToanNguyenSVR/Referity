package online.referity.repository;

import online.referity.entity.Job;
import online.referity.enums.JobLevel;
import online.referity.enums.JobStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job , UUID> {

    Job getById (UUID jobId);
    Job findJobById (UUID id);

    @Query(value = "SELECT j FROM Job j WHERE " +
            " j.status = 'ACTIVE' AND " +
            " j.isDelete = false AND "  +
            " ( :sFrom IS NULL OR j.salaryForm > :sFrom ) AND" +
            " ( :sTo IS NULL OR j.salaryTo < :sTo) AND"        +
//            " ( j.id In ( SELECT sl.job.id FROM SkillLevel sl WHERE sl.skill.id In (:skills) )) AND "               +
//            " ( j.id In (SELECT ll.job.id FROM LanguageLevel ll WHERE ll.programLanguage.id In :languages )) AND  " +
            " ( :workingMode IS NULL OR j.workingMode.id = :workingMode ) AND " + " (:city IS NULL OR j.campus.city.id = :city ) AND " +
            " ( :key IS NULL OR j.title Like %:key% ) AND" +
            " ( j.level In (:level) ) "  )
    List<Job> findAll(
             @Param("sFrom") Integer sFrom
            , @Param("sTo") Integer sTo
//            , @Param("reward") int reward
//            , @Param("skills") List<UUID> skills
//            , @Param("languages") List<UUID> languages
            , @Param("workingMode") UUID workingMode
            , @Param("city") UUID city
            , @Param("key") String key
            , @Param("level")List<JobLevel> level);
    @Query("SELECT j FROM Job j WHERE j.status = 'PENDING' AND j.companyStaff.company.id = ?1 ")
    List<Job> findVerifyJobByCompanyId(Sort sort , UUID companyId);
    @Query("SELECT j FROM Job j WHERE j.status != 'PENDING'  AND j.companyStaff.company.id = ?1 ")
    List<Job> findActiveJobByCompanyId(Sort sort , UUID companyId);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE'")
    List<Job> findActiveJob();

//    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE'  AND j.candidateApply.status = 'CONFIRM' ")
//    List<Job> findJobConfirm(Sort sort );
    @Query("SELECT j FROM Job j JOIN j.companyStaff s JOIN s.company c WHERE c.id = ?1")
    List<Job> getCompanyJob(UUID companyId);

    @Query("SELECT j FROM Job j   WHERE j.assignTo.id = ?1")
    List<Job> getStaffJob(UUID staffId);

    @Query("SELECT j.status ,  COUNT(j) FROM Job j JOIN j.companyStaff c WHERE c.company.id = :companyId AND j.createAt > :fromTime And j.createAt < :toTime Group By j.status  " )
    String[] countJobByCompanyId(@Param("companyId")UUID companyId , @Param("fromTime") LocalDateTime fromTime  , @Param("toTime") LocalDateTime toTime);
    @Query("SELECT COUNT(j) FROM Job j  " )
    int countJob();


    @Query("SELECT j.status ,  COUNT(j) FROM Job j JOIN j.companyStaff c WHERE c.id = :companyStaffId AND j.createAt > :fromTime And j.createAt < :toTime Group By j.status  " )
    String[] countJobByCompanyStaffId(@Param("companyStaffId")UUID companyStaffId , @Param("fromTime") LocalDateTime fromTime  , @Param("toTime") LocalDateTime toTime);




}

