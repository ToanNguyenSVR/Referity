package online.referity.repository;

import online.referity.entity.CompanyStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CompanyStaffRepository extends JpaRepository<CompanyStaff, UUID> {

    List<CompanyStaff> findByCompanyId(UUID id);
    List<CompanyStaff> findByCompanyIdAndIsDeletedFalse(UUID id);

//    @Query("SELECT cs FROM CompanyStaff cs WHERE cs.id = ?1 ")
    CompanyStaff findByIdAndIsDeletedFalse (UUID companyStaffId);
}
