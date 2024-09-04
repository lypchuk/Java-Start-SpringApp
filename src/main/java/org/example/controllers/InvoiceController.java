package org.example.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.example.model.InvoiceCreateModel;
import org.example.model.InvoiceUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.example.exception.InvoiceNotFoundException;
import org.example.model.Invoice;
import org.example.service.IInvoiceService;
import org.example.storage.impl.StorageService;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {


    @Value("${image.storage}")
    private String uploadPath;

    private Path rootLocation;

    private final StorageService storageService;

    @Autowired
    public InvoiceController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    private IInvoiceService service;

    @GetMapping("/")
    public String showHomePage() {
        return "homePage";
    }

    @GetMapping("/register")
    public String showRegistration() {
        return "registerInvoicePage";
    }

//    @PostMapping("/save")
//    public String saveInvoice(
//            @ModelAttribute Invoice invoice,
//            Model model
//    ) {
//        service.saveInvoice(invoice);
//        Long id = service.saveInvoice(invoice).getId();
//        String message = "Record with id : '"+id+"' is saved successfully !";
//        model.addAttribute("message", message);
//        return "registerInvoicePage";
//    }


//    @PostMapping("/save")
//    public String saveInvoice(
//            @ModelAttribute Invoice invoice,
//            //Model model,
//            RedirectAttributes attributes,
//            @RequestParam("file") MultipartFile file,
//            RedirectAttributes redirectAttributes
//    ) throws IOException {
//        rootLocation = Paths.get(uploadPath);
//
//            //Create custom filename
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//
//        File f = File.createTempFile("img_", ".webp",new File("C:/temp/"));
//        //f.getName();
//            //remove spaces and make lowercase
//        filename = filename.toLowerCase().replaceAll(" ", "-");
//
//
//        try {
//            if (file.isEmpty()) {
//                throw new StorageException("Failed to store empty file " + filename);
//            }
//            if (filename.contains("..")) {
//                // This is a security check
//                throw new StorageException(
//                        "Cannot store file with relative path outside current directory "
//                                + filename);
//            }
//            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
//                    StandardCopyOption.REPLACE_EXISTING);
//
//            //storedLocation = imageCDN+filename;
//
//        }
//        catch (IOException e) {
//            throw new StorageException("Failed to store file " + filename, e);
//        }
//
//        invoice.setImageName(filename);
//        //invoice.setImageName(file.getOriginalFilename());
//        service.saveInvoice(invoice);
//        Long id = service.saveInvoice(invoice).getId();
//        //String message = "Record with id : '"+id+"' is saved successfully !";
////        model.addAttribute("message", message);
////        return "registerInvoicePage";
//
//        storageService.store(file);
//
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");
//
//        attributes.addAttribute("message", "Record with id : '"+id+"' is saved successfully !");
//
//        return "redirect:getAllInvoices";
//    }

    @PostMapping("/save")
    public String saveInvoice(
            @ModelAttribute InvoiceCreateModel model,
            //  Model model,
            RedirectAttributes attributes
    ) {
//        service.saveInvoice(model);
        Long id = service.saveInvoice(model).getId();

        attributes.addAttribute("message", "Record with id : '"+id+"' is saved successfully !");
        return "redirect:getAllInvoices";
    }

    @GetMapping("/getAllInvoices")
    public String getAllInvoices(
            @RequestParam(value = "message", required = false) String message,
            Model model
    ) {
        List<Invoice> invoices= service.getAllInvoices();
        model.addAttribute("list", invoices);
        model.addAttribute("message", message);
        return "allInvoicesPage";
    }

    @GetMapping("/edit")
    public String getEditPage(
            Model model,
            RedirectAttributes attributes,
            @RequestParam Long id
    ) {
        String page = null;
        try {
            Invoice invoice = service.getInvoiceById(id);
            model.addAttribute("invoice", invoice);
            page="editInvoicePage";
        } catch (InvoiceNotFoundException e) {
            e.printStackTrace();
            attributes.addAttribute("message", e.getMessage());
            page="redirect:getAllInvoices";
        }
        return page;
    }

    @PostMapping("/update")
    public String updateInvoice(
            @ModelAttribute InvoiceUpdateModel invoiceUp,
            RedirectAttributes attributes
    ) throws IOException {


        service.updateInvoice(invoiceUp);
        Long id = invoiceUp.getId();
        attributes.addAttribute("message", "Invoice with id: '"+id+"' is updated successfully !");
        return "redirect:getAllInvoices";
    }

    @GetMapping("/delete")
    public String deleteInvoice(
            @RequestParam Long id,
            RedirectAttributes attributes
    ) {
        try {
            service.deleteInvoiceById(id);
            attributes.addAttribute("message", "Invoice with Id : '"+id+"' is removed successfully!");
        } catch (InvoiceNotFoundException e) {
            e.printStackTrace();
            attributes.addAttribute("message", e.getMessage());
        }
        return "redirect:getAllInvoices";
    }
}
