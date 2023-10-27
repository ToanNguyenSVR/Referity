package online.referity.repository;

import online.referity.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Company getById(UUID id);
    Company findCompanyById(UUID id);
    @Query("SELECT c FROM Company c WHERE c.status = 'VERIFY'")
    List<Company> getCompanyVerify();

    @Query("SELECT c FROM Company c WHERE c.status = 'ACTIVE'")
    List<Company> getCompanyActive();

}
