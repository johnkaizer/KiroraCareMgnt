package org.kcauniproject.kiroramanagementsystem.childrenManagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kcauniproject.kiroramanagementsystem.enums.ChildStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/children")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChildController {

    private final ChildService childService;

    @PostMapping
    public ResponseEntity<Child> createChild(@Valid @RequestBody Child child) {
        Child savedChild = childService.saveChild(child);
        return new ResponseEntity<>(savedChild, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id) {
        return childService.getChildById(id)
                .map(child -> ResponseEntity.ok().body(child))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Child>> getAllChildren() {
        List<Child> children = childService.getAllChildren();
        return ResponseEntity.ok(children);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Child>> getAllChildrenPaginated(Pageable pageable) {
        Page<Child> children = childService.getAllChildren(pageable);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Child>> getChildrenByStatus(@PathVariable ChildStatus status) {
        List<Child> children = childService.getChildrenByStatus(status);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Child>> searchChildren(@RequestParam String term) {
        List<Child> children = childService.searchChildren(term);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/admission-date-range")
    public ResponseEntity<List<Child>> getChildrenByAdmissionDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Child> children = childService.getChildrenByAdmissionDateRange(startDate, endDate);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/age-range")
    public ResponseEntity<List<Child>> getChildrenByAgeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Child> children = childService.getChildrenByAgeRange(startDate, endDate);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/count/{status}")
    public ResponseEntity<Long> getChildrenCountByStatus(@PathVariable ChildStatus status) {
        long count = childService.getChildrenCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @Valid @RequestBody Child childDetails) {
        try {
            Child updatedChild = childService.updateChild(id, childDetails);
            return ResponseEntity.ok(updatedChild);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChild(@PathVariable Long id) {
        if (childService.childExists(id)) {
            childService.deleteChild(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
