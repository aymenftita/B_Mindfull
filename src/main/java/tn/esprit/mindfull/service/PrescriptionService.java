package tn.esprit.mindfull.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.dtos.PrescriptionDto;
import tn.esprit.mindfull.entity.Prescription;
import tn.esprit.mindfull.entity.User;
import tn.esprit.mindfull.Mapper.PrescriptionMapper;
import tn.esprit.mindfull.Respository.PrescriptionRepository;
import tn.esprit.mindfull.Respository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionDto addPrescription(PrescriptionDto prescriptionDto) {
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDto);

        // Fetch and set the actual doctor and patient entities
        User doctor = userRepository.findById(prescriptionDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        User patient = userRepository.findById(prescriptionDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        prescription.setDoctor(doctor);
        prescription.setPatient(patient);

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(savedPrescription);
    }

    public List<PrescriptionDto> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    public PrescriptionDto getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        return prescriptionMapper.toDto(prescription);
    }

    public PrescriptionDto updatePrescription(Long id, PrescriptionDto prescriptionDto) {
        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        // Update basic fields
        existingPrescription.setMedicationName(prescriptionDto.getMedicationName());
        existingPrescription.setDosage(prescriptionDto.getDosage());

        // Update relationships if needed
        if (!existingPrescription.getDoctor().getUserId().equals(prescriptionDto.getDoctorId())) {
            User doctor = userRepository.findById(prescriptionDto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            existingPrescription.setDoctor(doctor);
        }

        if (!existingPrescription.getPatient().getUserId().equals(prescriptionDto.getPatientId())) {
            User patient = userRepository.findById(prescriptionDto.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            existingPrescription.setPatient(patient);
        }

        Prescription updatedPrescription = prescriptionRepository.save(existingPrescription);
        return prescriptionMapper.toDto(updatedPrescription);
    }

    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
}