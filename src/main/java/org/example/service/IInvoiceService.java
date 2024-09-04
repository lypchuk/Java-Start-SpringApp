package org.example.service;

import java.io.IOException;
import java.util.List;
import org.example.model.Invoice;
import org.example.model.InvoiceCreateModel;
import org.example.model.InvoiceUpdateModel;

public interface IInvoiceService {

    //public Invoice saveInvoice(Invoice invoice);
    public Invoice saveInvoice(InvoiceCreateModel invoice);
    public List<Invoice> getAllInvoices();
    public Invoice getInvoiceById(Long id);
    public void deleteInvoiceById(Long id);
    public void updateInvoice(InvoiceUpdateModel invoice) throws IOException;

}