package online.referity.service;

import online.referity.entity.WorkingMode;

import java.util.List;
import java.util.UUID;

public interface WorkingModeService {

    public WorkingMode create (WorkingMode mode);
    public WorkingMode update (WorkingMode mode , UUID id );
    public WorkingMode delete (UUID id);

    public List<WorkingMode> get () ;

}
