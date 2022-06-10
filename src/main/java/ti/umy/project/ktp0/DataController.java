/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ti.umy.project.ktp0;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ti.umy.project.ktp0.coba.exceptions.NonexistentEntityException;

/**
 *
 * @author wachid
 */
@Controller
public class DataController {
    
    DataJpaController datactrl = new DataJpaController();
    List<Data> newdata = new ArrayList<>();
        
    @RequestMapping("/data")
    //@ResponseBody
    public String getDataKTP(Model model){
        int record = datactrl.getDataCount();
        String result="";
        try{
            newdata = datactrl.findDataEntities().subList(0, record);
        }
        catch (Exception e){
            result = e.getMessage();
        }
        model.addAttribute("goData", newdata);
        model.addAttribute("record", record);
        return "database";
    }
    
    @RequestMapping("/main")
    public String getMain(){
        return "menu";
    }
    
    @GetMapping("/tambah")
    public String doAdd(){
        return "create";
    }
    
    @PostMapping(value = "/simpandata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView simpandata(HttpServletRequest request, @RequestParam("foto") MultipartFile file) throws ParseException, IOException, Exception {
        Data data = new Data();
        
        String noKtp = request.getParameter("noktp");
        String nama = request.getParameter("nama");
        String tglLahir = request.getParameter("tgllahir");
        String jenisKelamin = request.getParameter("jeniskelamin");
        String alamat = request.getParameter("alamat");
        String agama = request.getParameter("agama");
        String status = request.getParameter("status");
        String pekerjaan = request.getParameter("pekerjaan");
        String kewarganegaraan = request.getParameter("warganegara");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tglLahir);
        
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] foto = file.getBytes();
        
        data.setNoktp(noKtp);
        data.setNama(nama);
        data.setTgllahir(date);
        data.setJeniskelamin(jenisKelamin);
        data.setAlamat(alamat);
        data.setAgama(agama);
        data.setStatus(status);
        data.setPekerjaan(pekerjaan);
        data.setWarganegara(kewarganegaraan);
        data.setBerlakuhingga("SEUMUR HIDUP");
        data.setFoto(foto);
        
        datactrl.create(data);
        return new RedirectView("/data");
    }
    
    
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Data data = new Data();
        try {
            data = datactrl.findData(id);
        } catch (Exception e) {
        }
        
        if (data.getFoto() != null) {
            byte[] photo = data.getFoto();
            String base64Image = Base64.getEncoder().encodeToString(photo);
            String imgLink = "data:image/jpg;base64,".concat(base64Image);
            model.addAttribute("photo", imgLink);
        } else {
            model.addAttribute("foto", "");
        }
        
        model.addAttribute("data", data);
        return "edit";
    }
    
    
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Data data = datactrl.findData(id);
        
        if (data.getFoto() != null) {
            byte[] photo = data.getFoto();
            String base64Image = Base64.getEncoder().encodeToString(photo);
            String imgLink = "data:image/jpg;base64,".concat(base64Image);
            model.addAttribute("photo", imgLink);
        } else {
            model.addAttribute("foto", "");
        }
        
        model.addAttribute("data", data);
        return "detail";
    }
    
    @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView update(@PathVariable String id, HttpServletRequest request, @RequestParam("foto") MultipartFile file) throws ParseException, IOException, Exception {
        Data data = new Data();
        
        String noKtp = request.getParameter("noktp");
        String nama = request.getParameter("nama");
        String tglLahir = request.getParameter("tgllahir");
        String jenisKelamin = request.getParameter("jeniskelamin");
        String alamat = request.getParameter("alamat");
        String agama = request.getParameter("agama");
        String status = request.getParameter("status");
        String pekerjaan = request.getParameter("pekerjaan");
        String kewarganegaraan = request.getParameter("warganegara");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tglLahir);
        
        if (file.isEmpty()) {
            Data data2 = datactrl.findData(Long.parseLong(id));
            data.setFoto(data2.getFoto());
        } else {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            byte[] foto = file.getBytes();
            data.setFoto(foto);
        }
        
        data.setId(Long.parseLong(id));
        data.setNoktp(noKtp);
        data.setNama(nama);
        data.setTgllahir(date);
        data.setJeniskelamin(jenisKelamin);
        data.setAlamat(alamat);
        data.setAgama(agama);
        data.setStatus(status);
        data.setPekerjaan(pekerjaan);
        data.setWarganegara(kewarganegaraan);
        data.setBerlakuhingga("SEUMUR HIDUP");
        
        datactrl.edit(data);
        return new RedirectView("/data");
    }
    @GetMapping("/delete/{id}")
    public RedirectView destroy(@PathVariable Long id) throws NonexistentEntityException{
        Data data = datactrl.findData(id);
         try{
            if(data != null){
                datactrl.destroy(id);
            }
        }catch(Exception e){
            
        }
        return new RedirectView("/data");
    }
}
