package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceUpdateModel {
    private Long id;
    private String name;
    private String location;
    private MultipartFile image;
    private Double amount;
}