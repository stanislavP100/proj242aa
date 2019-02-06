package com.proj242.proj242.controller;

import com.proj242.proj242.entity.Measurement;
import com.proj242.proj242.entity.User;
import com.proj242.proj242.imgproj.ImgData;
import com.proj242.proj242.imgproj.Viy012;
import com.proj242.proj242.repository.MeasurementRepository;
import com.proj242.proj242.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MeasurementRepository measurementRepository;

    public static String uploadsDir = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/main")
    public String getMainPage(@AuthenticationPrincipal User user,
                              Model model)
    {
        List<Measurement> measurements = measurementRepository.findAllByUser(user);

        Collections.reverse(measurements);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("measurements", measurements);


        return "main";
    }

    @PostMapping("/mainSave")
    public String measurementSave(@AuthenticationPrincipal User user,
                                  @RequestParam String description,
                                  @RequestParam("file") MultipartFile file,
                                  Model model) throws Exception
    {
        String uuidFile = UUID.randomUUID().toString();
        String finalFileNameOriginal = uuidFile + ".original." + file.getOriginalFilename();
        String finalFileNameProcessing = uuidFile + ".processing." + file.getOriginalFilename();

        StringBuilder fileNameOriginal = new StringBuilder();
        StringBuilder fileNameProcessing = new StringBuilder();

        Path fileNameAndPathOriginal = Paths.get(MainController.uploadsDir, finalFileNameOriginal);
        Path fileNameAndPathProcessing = Paths.get(MainController.uploadsDir, finalFileNameProcessing);

        fileNameOriginal.append(finalFileNameOriginal);
        fileNameProcessing.append(finalFileNameProcessing);

        //inline start

        InputStream in = new ByteArrayInputStream(file.getBytes());
        BufferedImage img = ImageIO.read(in);
        in.close();

        ImgData imgDat = new ImgData();

        imgDat.img = img;
        imgDat.getDataFromImg(imgDat.img);

        imgDat.area = Viy012.getAreaSize(imgDat.getClearBinMat(), imgDat.imgWidth, imgDat.imgHeight, 0);
        imgDat.perimetr = Viy012.getPerimetr(imgDat.getClearBinMat(), imgDat.imgWidth, imgDat.imgHeight, 0);

        int[][] perimArray = Viy012.getPerimetrArray(imgDat.getClearBinMat(), imgDat.imgWidth, imgDat.imgHeight, 0);
        imgDat.setAreaMatrix(imgDat.getClearBinMat());
        imgDat.setPerimetrMatrix(perimArray);
        imgDat.diametr = Viy012.getDeametr(imgDat.getPerimetrMatrix(), imgDat.imgWidth, imgDat.imgHeight, 0);
        int[][] diamArray = Viy012.getDiametrArraay(imgDat.getPerimetrMatrix(), imgDat.imgWidth, imgDat.imgHeight, 0);
        imgDat.setDiametrMatrix(diamArray);
        int[][] convexHullArray = Viy012.getConvexHullArraay(imgDat.getPerimetrMatrix(), imgDat.imgWidth, imgDat.imgHeight, 0);
        imgDat.setBoundMatrix(convexHullArray);
        int[][] arrOne = imgDat.getAreaMatrix(),
                arrTwo = imgDat.getBoundMatrix();

        int countOne = 0,
                countTwo = 0;
        for (int i1 = 0; i1 < imgDat.imgWidth; i1++) {
            for (int j1 = 0; j1 < imgDat.imgHeight; j1++) {
                if (arrOne[i1][j1] == 0) {
                    countOne++;
                }

                if (arrTwo[i1][j1] == 0) {
                    countTwo++;
                }
            }
        }

        imgDat.bound = (float) countOne / (float) countTwo * 100.0f;
        imgDat.bound = (float) new BigDecimal((float) countOne / (float) countTwo * 100.0f).setScale(2, RoundingMode.UP).doubleValue();
        int[] centerCoord = Viy012.getCenterCoordArraay(imgDat.getAreaMatrix(), imgDat.imgWidth, imgDat.imgHeight, 0);
        imgDat.setCenterCoord(centerCoord);
        imgDat.assymetric = Viy012.getAssymetric(imgDat.getAreaMatrix(), imgDat.imgWidth, imgDat.imgHeight, imgDat.getCenterCoord(), 0);
        boolean [] colorArr = Viy012.getColorArray(imgDat.img, imgDat.getAreaMatrix(), imgDat.imgWidth, imgDat.imgHeight, 0);


        Viy012.imageSavePathParam(
                imgDat.img,
                imgDat.getAreaMatrix(),
                imgDat.getPerimetrMatrix(),
                imgDat.getDiametrMatrix(),
                imgDat.getBoundMatrix(),
                imgDat.imgWidth,
                imgDat.imgHeight,
                fileNameAndPathProcessing.toString(),
                0);

        in = new ByteArrayInputStream(file.getBytes());
        img = ImageIO.read(in);
        in.close();

        ImageIO.write(img, "png", new File(fileNameAndPathOriginal.toString()));


        //inline finished

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd (HH:mm:ss)");

        Measurement measurement = new Measurement();

        measurement.setDescription(description);
        measurement.setUser(user);
        measurement.setLocalDateTime(LocalDateTime.now());
        measurement.setStringData(dtf.format(measurement.getLocalDateTime()));

        measurement.setOriginalImage(fileNameOriginal.toString());
        measurement.setProccessingImage(fileNameProcessing.toString());


        measurement.setArea(imgDat.area);
        measurement.setDiameter(imgDat.diametr);
        measurement.setPerimeter(imgDat.perimetr);
        measurement.setAsymmetry(imgDat.assymetric);
        measurement.setBounds(imgDat.bound);

        this.measurementRepository.save(measurement);

        List<Measurement> measurements = this.measurementRepository.findAllByUser(user);
        Collections.reverse(measurements);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("measurements", measurements);

        return "redirect:/main";
    }

    @PostMapping("/mainDelete")
    public String measurementDelete(@RequestParam Long id)
    {
        measurementRepository.deleteById(id);

        return "redirect:/main";
    }


    @GetMapping("/findOne")
    @ResponseBody
    public Measurement findOneMeasurement(Long id) {

        List<Measurement> measurements = measurementRepository.findAll();

        for (int i = 0; i < measurements.size(); i++) {
            if (measurements.get(i).getId() == id) {
                return measurements.get(i);
            }
        }

        return null;
    }


    @GetMapping("/")
    public String getGreetingPage()
    {
        return "greeting";
    }
}

