package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import tn.esprit.mindfull.dtos.PrescriptionDto;
import tn.esprit.mindfull.service.PrescriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @PostMapping
    public PrescriptionDto addPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        return prescriptionService.addPrescription(prescriptionDto);
    }

    @GetMapping
    public List<PrescriptionDto> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/{id}")
    public PrescriptionDto getPrescriptionById(@PathVariable Long id) {
        return prescriptionService.getPrescriptionById(id);
    }

    @PutMapping("/{id}")
    public PrescriptionDto updatePrescription(@PathVariable Long id, @RequestBody PrescriptionDto prescriptionDto) {
        return prescriptionService.updatePrescription(id, prescriptionDto);
    }

    @DeleteMapping("/{id}")
    public void deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
    }
}