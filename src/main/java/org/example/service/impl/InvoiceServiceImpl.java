package org.example.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.example.model.InvoiceUpdateModel;
import org.example.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.exception.InvoiceNotFoundException;
import org.example.model.InvoiceCreateModel;
import org.example.model.Invoice;
import org.example.repo.InvoiceRepository;
import org.example.service.IInvoiceService;

@Service
public class InvoiceServiceImpl implements IInvoiceService {

    @Autowired
    private InvoiceRepository repo;

    @Autowired
    private StorageService storageService;

    @Override
    public Invoice saveInvoice(InvoiceCreateModel model) {
        try {
            Invoice invoice = new Invoice();
            invoice.setName(model.getName());
            invoice.setAmount(model.getAmount());
            var imageName = storageService.save(model.getImage());
            invoice.setImage(imageName);
            invoice.setLocation(model.getLocation());
            return repo.save(invoice);
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return repo.findAll();
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        Optional<Invoice> opt = repo.findById(id);
        if(opt.isPresent()) {
            return opt.get();
        } else {
            throw new InvoiceNotFoundException("Invoice with Id : "+id+" Not Found");
        }
    }

    @Override
    public void deleteInvoiceById(Long id) {
        try {
            Optional<Invoice> opt = repo.findById(id);
            if(opt.isPresent()) {
                storageService.delete(opt.get().getImage());
            } else {
                throw new InvoiceNotFoundException("Invoice with Id : "+id+" Not Found");
            }

        }
        catch (Exception ex) {
            //ex.getMessage();
        }
        repo.delete(getInvoiceById(id));
    }

    @Override
    public void updateInvoice(InvoiceUpdateModel invoiceUp) throws IOException {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceUp.getId());
        invoice.setName(invoiceUp.getName());
        invoice.setAmount(invoiceUp.getAmount());
        invoice.setLocation(invoiceUp.getLocation());

        Invoice invoiceOld = this.getInvoiceById(invoiceUp.getId());

        String newName = storageService.updateFile(invoiceOld.getImage(),invoiceUp.getImage());
        invoice.setImage(newName);

        repo.save(invoice);
    }
}