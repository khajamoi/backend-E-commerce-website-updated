package com.fruit_ecommerce_backend.fruit_ecommerce.service;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.MedicalReport;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.MedicalReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalReportService {

    private final MedicalReportRepository repository;

    public List<MedicalReport> listAll() {
        return repository.findAll();
    }

    public Optional<MedicalReport> findById(Long id) {
        return repository.findById(id);
    }

    public MedicalReport save(MedicalReport report) {
        return repository.save(report);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<MedicalReport> saveAll(List<MedicalReport> reports) {
        return repository.saveAll(reports);
    }
    public MedicalReport updateReport(Long id, MedicalReport updated) {
        return repository.findById(id)
                .map(existing -> {
                    // copy all fields
                    existing.setAgeRange(updated.getAgeRange());
                    existing.setBodyPart(updated.getBodyPart());
                    existing.setBookingCutoff(updated.getBookingCutoff());
                    existing.setCenterName(updated.getCenterName());
                    existing.setColorName(updated.getColorName());
                    existing.setComponent(updated.getComponent());
                    existing.setCondition(updated.getCondition());
                    existing.setContainer(updated.getContainer());
                    existing.setDepartment(updated.getDepartment());
                    existing.setDepartmentName(updated.getDepartmentName());
                    existing.setDisease(updated.getDisease());
                    existing.setDoctorSpecialty(updated.getDoctorSpecialty());
                    existing.setFastingTime(updated.getFastingTime());
                    existing.setGender(updated.getGender());
                    existing.setHomeCollection(updated.getHomeCollection());
                    existing.setInvestigationName(updated.getInvestigationName());
                    existing.setLab(updated.getLab());
                    existing.setLocationName(updated.getLocationName());
                    existing.setPackageName(updated.getPackageName());
                    existing.setParameters(updated.getParameters());
                    existing.setPreRequisites(updated.getPreRequisites());
                    existing.setPrescription(updated.getPrescription());
                    existing.setPrice(updated.getPrice());
                    existing.setPriceUpdatedOn(updated.getPriceUpdatedOn());
                    existing.setProcessingDays(updated.getProcessingDays());
                    existing.setQuantity(updated.getQuantity());
                    existing.setReportDeliveryDays(updated.getReportDeliveryDays());
                    existing.setReportTat(updated.getReportTat());
                    existing.setReportingCutoff(updated.getReportingCutoff());
                    existing.setSampleReceiveTat(updated.getSampleReceiveTat());
                    existing.setSampleType(updated.getSampleType());
                    existing.setSampleTypeName(updated.getSampleTypeName());
                    existing.setSpecimen(updated.getSpecimen());
                    existing.setSracutoff(updated.getSracutoff());
                    existing.setStabilityRefrigerated(updated.getStabilityRefrigerated());
                    existing.setStabilityRoom(updated.getStabilityRoom());
                    existing.setSynonymous(updated.getSynonymous());
                    existing.setTemperature(updated.getTemperature());
                    existing.setTestCode(updated.getTestCode());
                    existing.setTestMethod(updated.getTestMethod());
                    existing.setTestName(updated.getTestName());
                    existing.setTestUpdatedOn(updated.getTestUpdatedOn());
                    existing.setUsage(updated.getUsage());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Report not found with id " + id));
    }
}
