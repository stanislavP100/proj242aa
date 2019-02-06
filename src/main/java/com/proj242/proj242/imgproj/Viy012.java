package com.proj242.proj242.imgproj;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

//Вий 012
public class Viy012
{
    //Загрузка изображения по его пути
    static BufferedImage imageLoadPath(String path) throws Exception
    {
        File file = new File(path);
        return ImageIO.read(file);
    }

    //Сохранине изображения по пути с указанием ширины и высоты изображения
    public static void imageSavePath(int imgMatrix[][], int imgWidth, int imgHeight, String path) throws Exception
    {
        BufferedImage img = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_BGR);

        for(int i=0;i<img.getWidth();i++)
        {
            for(int j=0;j<img.getHeight();j++)
            {
                Color clr=new Color(imgMatrix[i][j],imgMatrix[i][j],imgMatrix[i][j]);
                img.setRGB(i, j, clr.getRGB());
            }
        }


        ImageIO.write(img, "png", new File(path));
    }

    //Преобразование двухмерного массива в изображение с указанием ширины и высоты изображения
    static BufferedImage arrayToImg(int imgMatrix[][], int imgWidth, int imgHeight) throws Exception
    {
        BufferedImage img = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_BGR);

        for(int i=0;i<img.getWidth();i++)
        {
            for(int j=0;j<img.getHeight();j++)
            {
                Color clr=new Color(imgMatrix[i][j],imgMatrix[i][j],imgMatrix[i][j]);
                img.setRGB(i, j, clr.getRGB());
            }
        }

        return img;
    }


    //Получение матрицы уровней градаций серого (метод среднего) из изображения с указанной шириной и высотой
    static int[][] getGrayscaleMatrixAverage(BufferedImage img, int imgWidth, int imgHeight)
    {
        int
                grayscaleMatrix[][] = new int[imgWidth][imgHeight];

        int
                r = 0,
                g = 0,
                b = 0,
                aver = 0;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                Color clr = new Color(img.getRGB(i, j));
                r = clr.getRed();
                g = clr.getGreen();
                b = clr.getBlue();

                aver = (int)((r + g + b)/3);

                grayscaleMatrix[i][j] = aver;

            }
        }

        return grayscaleMatrix;

    }

    //Получение матрицы уровней градаций серого (метод десатурации) из изображения с указанной шириной и высотой
    public static int[][] getGrayscaleMatrixDesaturation(BufferedImage img, int imgWidth, int imgHeight)
    {
        int[][] greyScale = new int[imgWidth][imgHeight];

        int[] greyScaleValue = new int[3];

        for (int i = 0; i < imgWidth; i++)
        {
            for (int j = 0; j < imgHeight; j++)
            {
                Color clr = new Color(img.getRGB(i, j));
                greyScaleValue[0] = clr.getRed();
                greyScaleValue[1] = clr.getGreen();
                greyScaleValue[2] = clr.getBlue();

				/*
                System.out.println("r: " + greyScaleValue[0]);
                System.out.println("g: " + greyScaleValue[1]);
                System.out.println("b: " + greyScaleValue[2]);
                */

                int max = 0,
                        min = 255;

                for(int iColor = 0; iColor < 3; iColor++)
                {
                    if(greyScaleValue[iColor] > max)
                    {
                        max = greyScaleValue[iColor];
                    }

                    if(greyScaleValue[iColor] < min)
                    {
                        min = greyScaleValue[iColor];
                    }
                }

                greyScale[i][j] = (max + min) / 2;

                /*
				System.out.println("max: " + max);
				System.out.println("min: " + min);

                System.out.println("Grey value: " + greyScale[i][j]);
                System.out.println();
                try {Thread.sleep(2000);}catch(Exception ex){}
                */

            }
        }

        return greyScale;
    }

    //Получение ширины изображения
    static int getImgWidth(BufferedImage img)
    {
        return img.getWidth();
    }

    //Получение высоты изображения
    static int getImgHeight(BufferedImage img)
    {
        return img.getHeight();
    }


    //получение бинаризированной матрицы по среднему значению общего порога
    static public int[][] getBinMatrixAverage(int[][] arrayImg, int imgWidth, int imgHeight)
    {

        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] <= 127)
                {
                    arrayArea[i][j] = 0;
                }
                else
                {
                    arrayArea[i][j] = 255;
                }
            }
        }

        return arrayArea;
    }

    //бинаризация изображения методом Отсу
    static public int[][] getBinMatrixMethodOtsu(int[][] arrayImg, int imgWidth, int imgHeight)
    {

        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }



        List<Integer> partList = new ArrayList<Integer>();

        for(int i = 0; i < 256; i++)
        {
            partList.add(0);
        }

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                partList.set(arrayArea[i][j], partList.get(arrayArea[i][j])+1);
            }
        }

        for(int i = 0; i < partList.size(); i++)
        {
            //System.out.println("hist[" + i + "]: " + partList.get(i));
        }

        int threshold = methodOtsu(partList);

        //System.out.println("Threshold: " + threshold);

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] < threshold)
                {
                    arrayArea[i][j] = 0;
                }
                else
                {
                    arrayArea[i][j] = 255;
                }
            }
        }

        return arrayArea;
    }

    //бинаризация изображения методом Отсу
    static public int[][] getBinMatrixMethodGlobalThreshold(int[][] arrayImg, int imgWidth, int imgHeight)
    {

        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }



        List<Integer> partList = new ArrayList<Integer>();

        for(int i = 0; i < 256; i++)
        {
            partList.add(0);
        }

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                partList.set(arrayArea[i][j], partList.get(arrayArea[i][j])+1);
            }
        }

        for(int i = 0; i < partList.size(); i++)
        {
            //System.out.println("hist[" + i + "]: " + partList.get(i));
        }

        //int threshold = methodOtsu(partList);
        int threshold = 50;

        //System.out.println("Threshold: " + threshold);

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] < threshold)
                {
                    arrayArea[i][j] = 0;
                }
                else
                {
                    arrayArea[i][j] = 255;
                }
            }
        }

        return arrayArea;
    }



    //реализация метода Отсу для списка целочисленных значений
    static public int methodOtsu(List<Integer> hist)
    {
        int treshold = 0;

        int hist_max = 0;
        int n_hist_max = 0;

        for (int i = 0; i < hist.size(); i++)
        {
            if (hist.get(i) > hist_max)
            {
                hist_max = hist.get(i);
                n_hist_max = i;
            }
        }

        int w_sum = 0;
        int mu_sum = 0;

        for (int i = 0; i < hist.size(); i++)
        {
            w_sum = hist.get(i) + w_sum;
            mu_sum = i * hist.get(i) + mu_sum;
        }

        double sigma_max = -1.0;
        double w1 = 0.0;
        double a = 0.0;
        double sigma = 0.0;
        double a1 = 0.0;
        double b1 = 0.0;

        for (int i = 0; i < hist.size(); i++)
        {
            a1 = (double)(i * hist.get(i)) + a1;

            b1 = (double)(hist.get(i)) + b1;

            w1 = b1 / ((double)(w_sum));

            a = a1 / b1 - (((double)(mu_sum)) - a1) / (((double)(w_sum)) - b1);

            sigma = w1 * (1.0 - w1) * a * a;

            //System.out.println("i: " + i + "| Sigma: " + sigma);

            if (sigma > sigma_max)
            {
                sigma_max = sigma;
                treshold = i;
            }
        }

        return treshold;
    }

    //Не работающий метод Соболева
    static public int[][] getMatrixMethodSoboleva(int[][] arrayImg, int imgWidth, int imgHeight)
    {

        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }


        BufferedImage img = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_BGR);

        /*
        double buff_0,
               buff_red,
               buff_green,
               buff_blue;
        */

        int g_x, g_y, g_grad;
        int p00, p01, p02,
                p10, p11, p12,
                p20, p21, p22;

        for (int i = 1; i < imgWidth - 1; i++)
        {
            for (int j = 1; j < imgHeight - 1; j++)
            {
                p00 = arrayImg[i - 1][j - 1];
                p01 = arrayImg[i - 1][j];
                p02 = arrayImg[i - 1][j + 1];

                p10 = arrayImg[i][j - 1];
                p11 = arrayImg[i][j];
                p12 = arrayImg[i][j + 1];

                p20 = arrayImg[i + 1][j - 1];
                p21 = arrayImg[i + 1][j];
                p22 = arrayImg[i + 1][j + 1];

                g_y = -p00 - 2 * p01 - p02 + p20 + 2 * p21 + p22;
                g_x = -p00 - 2 * p10 - p20 + p02 + 2 * p12 + p22;

                g_grad = (int)Math.sqrt(g_x * g_x + g_y * g_y);

                if (g_grad > 255)
                {
                    g_grad = 255;
                }

                arrayArea[i][j] = (int)g_grad;

            }
        }

        for (int i = 0; i < imgWidth; i++)
        {
            arrayArea[i][0] = arrayArea[i][1];
            arrayArea[i][imgHeight - 1] = arrayArea[i][imgHeight - 2];
        }

        for (int j = 0; j < imgHeight; j++)
        {
            arrayArea[0][j] = arrayArea[1][j];
            arrayArea[imgWidth - 1][j] = arrayArea[imgWidth - 2][j];
        }

        return arrayArea;
    }

    //Метод разбиеения участков бинаризированного изображения
    //на отдельные не связанные участки
    static public int[][] getAreaMatrixRaw(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        //System.out.println();
        int mark = 0;
        List<Integer> markList = new ArrayList<Integer>();

        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }

    	/*
    	for(int i = 0; i < imgWidth; i++)
    	{
    		for(int j = 0; j < imgHeight; j++)
    		{
    			if(arrayArea[i][j] == 0)
    			{
    				mark--;
    				arrayArea[i][j] = mark;
    				arrayArea = getMarkArea(arrayArea, imgWidth, imgHeight, i, j, mark);
    				//System.out.println("area: \ti: " + i + " |\t j: "+j);
    			}
    		}
    	}
    	*/

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] == 0)
                {
                    mark--;
                    markList.add(mark);
                    arrayArea[i][j] = mark;;
                }

                if(arrayArea[i][j] == mark)
                {
                    try{if(arrayArea[i - 1][j - 1] == 0) {arrayArea[i - 1][j - 1] = mark;}}catch(Exception ex) {};
                    try{if(arrayArea[i - 1][j - 0] == 0) {arrayArea[i - 1][j - 0] = mark;}}catch(Exception ex) {};
                    try{if(arrayArea[i - 1][j + 1] == 0) {arrayArea[i - 1][j + 1] = mark;}}catch(Exception ex) {};

                    try{if(arrayArea[i - 0][j - 1] == 0) {arrayArea[i - 0][j - 1] = mark;}}catch(Exception ex) {};
                    try{if(arrayArea[i - 0][j - 0] == 0) {arrayArea[i - 0][j - 0] = mark;}}catch(Exception ex) {};
                    try{if(arrayArea[i - 0][j + 1] == 0) {arrayArea[i - 0][j + 1] = mark;}}catch(Exception ex) {};

                    try{if(arrayArea[i + 1][j - 1] == 0) {arrayArea[i + 1][j - 1] = mark;}}catch(Exception ex) {};
                    try{if(arrayArea[i + 1][j - 0] == 0) {arrayArea[i + 1][j - 0] = mark;}}catch(Exception ex) {};
                    try{if(arrayArea[i + 1][j + 1] == 0) {arrayArea[i + 1][j + 1] = mark;}}catch(Exception ex) {};

                }
            }
        }

        int counterChange = 0;
        for(int iList = 0; iList < markList.size(); iList++)
        {
            counterChange = -1;
            while(counterChange != 0)
            {
                counterChange = 0;

                for(int i = 0; i < imgWidth; i++)
                {
                    for(int j = 0; j < imgHeight; j++)
                    {
                        if(arrayArea[i][j] == markList.get(iList))
                        {
                            mark = markList.get(iList);
                            try{if(arrayArea[i - 1][j - 1] < mark) {arrayArea[i - 1][j - 1] = mark; counterChange++;}}catch(Exception ex) {};
                            try{if(arrayArea[i - 1][j - 0] < mark) {arrayArea[i - 1][j - 0] = mark; counterChange++;}}catch(Exception ex) {};
                            try{if(arrayArea[i - 1][j + 1] < mark) {arrayArea[i - 1][j + 1] = mark; counterChange++;}}catch(Exception ex) {};

                            try{if(arrayArea[i - 0][j - 1] < mark) {arrayArea[i - 0][j - 1] = mark; counterChange++;}}catch(Exception ex) {};
                            try{if(arrayArea[i - 0][j - 0] < mark) {arrayArea[i - 0][j - 0] = mark; counterChange++;}}catch(Exception ex) {};
                            try{if(arrayArea[i - 0][j + 1] < mark) {arrayArea[i - 0][j + 1] = mark; counterChange++;}}catch(Exception ex) {};

                            try{if(arrayArea[i + 1][j - 1] < mark) {arrayArea[i + 1][j - 1] = mark; counterChange++;}}catch(Exception ex) {};
                            try{if(arrayArea[i + 1][j - 0] < mark) {arrayArea[i + 1][j - 0] = mark; counterChange++;}}catch(Exception ex) {};
                            try{if(arrayArea[i + 1][j + 1] < mark) {arrayArea[i + 1][j + 1] = mark; counterChange++;}}catch(Exception ex) {};

                        }
                    }
                }
            }
        }

        markList.clear();


        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] > 0)
                {
                    arrayArea[i][j] = 0;
                }

                if(arrayArea[i][j] < 0 && !markList.contains(arrayArea[i][j]))
                {
                    markList.add(arrayArea[i][j]);
                }
            }
        }


        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(markList.contains(arrayArea[i][j]))
                {
                    arrayArea[i][j] = markList.indexOf(arrayArea[i][j]) + 1;
                }
            }
        }

        markList.clear();
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] > 0 && !markList.contains(arrayArea[i][j]))
                {
                    markList.add(arrayArea[i][j]);
                    //System.out.println("i: " + i + " | mark: " + arrayArea[i][j]);
                }
            }
        }

    	/*
    	System.out.println();

    	int maxMark = 0;

    	for(int i = 0; i < markList.size(); i++)
    	{
			System.out.println("current markList: " + markList.get(i));

    		if(maxMark < markList.get(i))
    		{
    			maxMark = markList.get(i);
    		}
    	}

    	System.out.println("Area size: " + maxMark);

    	System.out.println();
    	*/

        return arrayArea;
    }

    //Метод подсчета числа отдельных не связанных участков изображения
    //по их уникальным маскам
    static public int getAreaCount(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        int areaCount = 0;

        List<Integer> markList = new ArrayList<Integer>();

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] > 0 && !markList.contains(arrayImg[i][j]))
                {
                    markList.add(arrayImg[i][j]);
                }
            }
        }

        areaCount = markList.size();

        return areaCount;
    }

    //Метод перемаркировки отдельных не связанных участков изображения
    //для получения минимального числа положетельных масок
    public static int [][] getRemarkOrderMatrix(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }

        List<Integer> markList = new ArrayList<Integer>();

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] > 0  && !markList.contains(arrayArea[i][j]))
                {
                    markList.add(arrayArea[i][j]);
                }
            }
        }

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(markList.contains(arrayArea[i][j]))
                {
                    arrayArea[i][j] = markList.indexOf(arrayArea[i][j]) + 1;
                }
            }
        }

        return arrayArea;
    }

    //Разметка отдельной области сегментированной матрицы изображения на основе начального зерна участка
    static public int[][] getMarkArea(int [][] arrayImg, int imgWidth, int imgHeight, int iStart, int jStart, int mark)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }

        int size = Math.max(imgWidth, imgHeight);

        //System.out.println("Size: " + size);

        for(int step = 0; step < size; step++)
        {
            for(int i = iStart - step; i <= iStart + step; i++)
            {
                for(int j = jStart - step; j <= jStart; j++)
                {
                    try {if(arrayArea[i][j] == 0){arrayArea = checkNearArea(arrayArea, imgWidth, imgHeight, i, j, mark);}}catch(Exception ex){};
                }
            }
        }

        return arrayArea;
    }

    //Получение списка уникальных масок отдельных областей сегментированного изображения
    static public List<Integer> getMarkList(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        List<Integer> markList = new ArrayList<Integer>();

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] > 0  && !markList.contains(arrayImg[i][j]))
                {
                    markList.add(arrayImg[i][j]);
                }
            }
        }

        return markList;
    }

    //Проверка соседей пикселя со связностью 8 на наличие соседей с текущей маркировкой
    static public int[][] checkNearArea(int [][] arrayImg, int imgWidth, int imgHeight, int iStart, int jStart, int mark)
    {
        boolean bArr[] = new boolean[9];

        for(int i = 0; i < bArr.length; i++)
        {
            bArr[i] = false;
        }

        try{if(arrayImg[iStart - 1][jStart - 1] == mark){bArr[0] = true;}}catch(Exception ex){};
        try{if(arrayImg[iStart - 1][jStart - 0] == mark){bArr[1] = true;}}catch(Exception ex){};
        try{if(arrayImg[iStart - 1][jStart + 1] == mark){bArr[2] = true;}}catch(Exception ex){};

        try{if(arrayImg[iStart - 0][jStart - 1] == mark){bArr[3] = true;}}catch(Exception ex){};
        try{if(arrayImg[iStart - 0][jStart - 0] == mark){bArr[4] = true;}}catch(Exception ex){};
        try{if(arrayImg[iStart - 0][jStart + 1] == mark){bArr[5] = true;}}catch(Exception ex){};

        try{if(arrayImg[iStart + 1][jStart - 1] == mark){bArr[6] = true;}}catch(Exception ex){};
        try{if(arrayImg[iStart + 1][jStart - 0] == mark){bArr[7] = true;}}catch(Exception ex){};
        try{if(arrayImg[iStart + 1][jStart + 1] == mark){bArr[8] = true;}}catch(Exception ex){};

        if(bArr[0] || bArr[1] || bArr[2] || bArr[3] || bArr[4] || bArr[5] || bArr[6] || bArr[7] || bArr[8])
        {
            try{if(arrayImg[iStart - 1][jStart - 1] == 0) {arrayImg[iStart - 1][jStart - 1] = mark;}}catch(Exception ex) {};
            try{if(arrayImg[iStart - 1][jStart - 0] == 0) {arrayImg[iStart - 1][jStart - 0] = mark;}}catch(Exception ex) {};
            try{if(arrayImg[iStart - 1][jStart + 1] == 0) {arrayImg[iStart - 1][jStart + 1] = mark;}}catch(Exception ex) {};

            try{if(arrayImg[iStart - 0][jStart - 1] == 0) {arrayImg[iStart - 0][jStart - 1] = mark;}}catch(Exception ex) {};
            try{if(arrayImg[iStart - 0][jStart - 0] == 0) {arrayImg[iStart - 0][jStart - 0] = mark;}}catch(Exception ex) {};
            try{if(arrayImg[iStart - 0][jStart + 1] == 0) {arrayImg[iStart - 0][jStart + 1] = mark;}}catch(Exception ex) {};

            try{if(arrayImg[iStart + 1][jStart - 1] == 0) {arrayImg[iStart + 1][jStart - 1] = mark;}}catch(Exception ex) {};
            try{if(arrayImg[iStart + 1][jStart - 0] == 0) {arrayImg[iStart + 1][jStart - 0] = mark;}}catch(Exception ex) {};
            try{if(arrayImg[iStart + 1][jStart + 1] == 0) {arrayImg[iStart + 1][jStart + 1] = mark;}}catch(Exception ex) {};

    		/*
			for(int iOut = 0; iOut < 8; iOut++)
			{
				for(int jOut = 0; jOut < 8; jOut++)
				{
					System.out.print("\t" + arrayImg[iOut][jOut]+ " ");
				}
				System.out.println();
			}
			System.out.println();
			*/

        }

        //System.out.println("check new Area works");

        return arrayImg;

    }

    //Формирование отдельной не связанной области бинаризированной
    //матрицы изображения по ее маске
    public static int[][] getMatrixSingleArea(int [][] arrayImg, int imgWidth, int imgHeight, int mark)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] != mark)
                {
                    arrayArea[i][j] = 0;
                }
                else
                {
                    arrayArea[i][j] = mark;
                }

            }
        }
        return arrayArea;
    }

    //Получение списка матрич несвязанных между собой областей на
    //основе списка уникальных масок этих областей
    public static List<int[][]> getListOfMatrixSingleArea(int [][] arrayImg, int imgWidth, int imgHeight, List<Integer> markList)
    {
        List<int[][]> matrixSinglAreaList = new ArrayList<int[][]>();

        for(int iMark = 0; iMark < markList.size(); iMark++)
        {
            int markArray[][] = new int[imgWidth][imgHeight];

            markArray = getMatrixSingleArea(arrayImg, imgWidth, imgHeight, markList.get(iMark));

            matrixSinglAreaList.add(markArray);
        }

        return matrixSinglAreaList;
    }

    //Получение списка размеров несвязанных между собой областей
    //сегментированного изображения масок
    public static List<Integer> getCountAreaSize(int [][] arrayImg, int imgWidth, int imgHeight, List<Integer> markList)
    {
        List<Integer> areaList = new ArrayList<Integer>();

        for(int i = 0; i < markList.size(); i++)
        {
            areaList.add(0);
        }

        int ind = 0;
        int val = 0;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] > 0)
                {
                    ind = markList.indexOf(arrayImg[i][j]);
                    val = areaList.get(ind) + 1;
                    areaList.set(ind, val);
                }
            }
        }

        return areaList;
    }

    //Получение сортированного списка размеров несвязанных между собой областей
    //секгментированного изображения масок
    public static List<Integer> getCountAreaSizeSort(List<Integer> areaList)
    {
        List<Integer> sortAreaList = new ArrayList<Integer>();

        for(int i = 0; i < areaList.size(); i++)
        {
            sortAreaList.add(areaList.get(i));
        }

        Collections.sort(sortAreaList);

        return sortAreaList;
    }

    //получение порога размера области, разбивающей отдельные не связанные между собой области
    //сегментированного изображения на мусор и объекты
    public static int areaSizeTreshold(List<Integer> areaCountSizeSortList)
    {
        int treshold = Viy012.methodOtsu(areaCountSizeSortList);

        return treshold;
    }

    //удаление малых по размеру несвязанных между собой отдельных областей
    //сегментированного изображения
    public static int[][] getMatrixWithoutLittleArea(int[][] arrayImg, int imgWidth, int imgHeight, List<Integer>areaCountSizeList)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }


        //int treshold = Viy012.areaSizeTreshold(areaCountSizeSortList);
        int treshold = Viy012.areaSizeTreshold(areaCountSizeList);

        for(int i = 0; i < areaCountSizeList.size(); i++)
        {
            if(areaCountSizeList.get(i) < treshold)
            {
                areaCountSizeList.set(i, 0);
            }
        }

        int markCurr = 0;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] > 0)
                {
                    //System.out.println("mark area: " + arrayArea[i][j]);
                    markCurr = arrayArea[i][j];

                    if(areaCountSizeList.get(markCurr - 1) == 0)
                    {

                        arrayArea[i][j] = 0;
                    }

                }

            }
        }

        return arrayArea;
    }


    //перевод матрицы сегметированных отдельных несвязанных областей
    //маркированного изображения в бинаризированное изображение
    public static int[][] convertAreaMarkMatrixToBinMatrix(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j]>0)
                {
                    arrayArea[i][j] = 0;
                }
                else
                {
                    arrayArea[i][j] = 255;
                }

            }
        }


        return arrayArea;
    }

    //Нанесение линии на матрицу с заданными точками начала и конца линии
    public static int[][] setLineOnMatrix(int[][] arrayImg, int imgWidth, int imgHeight, int[] arrCoord, int mark)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }

        int x1 = arrCoord[0],
                y1 = arrCoord[1],
                x2 = arrCoord[2],
                y2 = arrCoord[3];

        int dx = (x2 - x1 >= 0 ? 1 : -1);
        int dy = (y2 - y1 >= 0 ? 1 : -1);

        int lengthX = Math.abs(x2 - x1);
        int lengthY = Math.abs(y2 - y1);

        int length = Math.max(lengthX, lengthY);

        if (length == 0)
        {
            arrayArea[x1][y1] = mark;
        }

        if (lengthY <= lengthX)
        {
            // Начальные значения
            int x = x1;
            int y = y1;
            int d = -lengthX;

            // Основной цикл
            length++;
            while(length-- > 0)
            {
                arrayArea[x][y] = mark;
                x += dx;
                d += 2 * lengthY;
                if (d > 0) {
                    d -= 2 * lengthX;
                    y += dy;
                }
            }
        }
        else
        {
            // Начальные значения
            int x = x1;
            int y = y1;
            int d = - lengthY;

            // Основной цикл
            length++;
            while(length-- > 0)
            {
                arrayArea[x][y] = mark;
                y += dy;
                d += 2 * lengthX;
                if (d > 0) {
                    d -= 2 * lengthY;
                    x += dx;
                }
            }
        }



        return arrayArea;
    }

    //построение выпуклой оболочки в области по маскам этих точек
    public static int[][] setConvexHullOnMatrix(int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }

        int countMarkStart = 0,
                countMarkFinish = 0;

        List<int[]> coordList = new ArrayList<int[]>();

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] == mark)
                {
                    int coord[] = {i,j};
                    coordList.add(coord);
                    countMarkStart++;
                }

            }
        }

        for(int iStep = 0; iStep < coordList.size(); iStep++)
        {
            for(int jStep = 0; jStep < coordList.size(); jStep++)
            {
                int arrCoord [] = {coordList.get(iStep)[0], coordList.get(iStep)[1], coordList.get(jStep)[0], coordList.get(jStep)[1]};

                try
                {
                    arrayArea = setLineOnMatrix(arrayArea, imgWidth, imgHeight, arrCoord, mark);
                }
                catch(Exception ex)
                {

                }
            }
        }

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayArea[i][j] == mark)
                {
                    countMarkFinish++;
                }

            }
        }

        if(countMarkStart!=countMarkFinish)
        {
            arrayArea = setConvexHullOnMatrix(arrayArea, imgWidth, imgHeight, mark);
        }


        return arrayArea;
    }

    //Смена объекта и фона в бинаризинованной матрице
    public static int[][] getReversBinMatrix(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == 0)
                {
                    arrayArea[i][j] = 255;
                }
                else
                {
                    arrayArea[i][j] = 0;
                }
            }
        }

        return arrayArea;
    }


    //Получение многоступенчато очищенной от мелких деталей бинаризированной матрицы
    //исходного изображения
    public static int[][] getComplexClearBinMatrix(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }

        //Маркровка отдельных несвязанных областей сегментированного изображения
        int[][] areaMatrixRaw = Viy012.getAreaMatrixRaw(arrayArea.clone(), imgWidth, imgHeight);

        //Получение числа отдельных не связанных между собой участков сегментированного изображения
        //int areaCounterRaw = Viy012.getAreaCount(areaMatrixRaw, imgWidth, imgHeight);

        //Получение матрицы отдельных несвязанных областей сегметированного изображения
        //с переопределенными положительными масками и фоном, равном 0
        int [][] areaMatrixReoder = Viy012.getRemarkOrderMatrix(areaMatrixRaw, imgWidth, imgHeight);

        //Получение списка уникальных масок отдельных несвязанных областей сегментированного изображения
        List<Integer> markList = Viy012.getMarkList(areaMatrixReoder, imgWidth, imgHeight);

        //Получение списка размеов отдельных несвязанных областей сегментированного переопределенного изображения
        List<Integer>counterAreaList = Viy012.getCountAreaSize(areaMatrixRaw, imgWidth, imgHeight, markList);

        //Получение списка отдельных несвязанных областей на осове масок отдельных несвязанных областей
        //imgDat.matrixMarAreakList = Viy012.getListOfMatrixSingleArea(imgDat.areaMatrixRaw, imgDat.imgWidth, imgDat.imgHeight, imgDat.markList);

        //Запись матрицы бинаризировнного изображения в итоговое изображение
        //Viy012.imageSavePath(imgDat.binMatrix, imgDat.imgWidth, imgDat.imgHeight, "d:\\imgsal\\results\\binimg.png");


        int arr [][] = Viy012.getMatrixWithoutLittleArea(areaMatrixReoder, imgWidth, imgHeight, counterAreaList);


        int arr2[][] = Viy012.convertAreaMarkMatrixToBinMatrix(arr, imgWidth, imgHeight);

        arrayArea = arr2;

        return arrayArea;
    }


    //Полная очистка от малых частей, наверное
    public static int[][] getWithoutLittlePartBinMatrix(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        int[][] areaMatrixRaw = Viy012.getAreaMatrixRaw(arrayImg, imgWidth, imgHeight);
        int[][] areaMatrixRaw2 = areaMatrixRaw.clone();

        int [][] areaMatrixReoder = Viy012.getRemarkOrderMatrix(areaMatrixRaw, imgWidth, imgHeight);
        List<Integer> markList = Viy012.getMarkList(areaMatrixReoder, imgWidth, imgHeight);
        List<Integer>counterAreaList = Viy012.getCountAreaSize(areaMatrixRaw, imgWidth, imgHeight, markList);
        List<Integer>counterAreaListSort = Viy012.getCountAreaSizeSort(counterAreaList);
        int areaThreshold = Viy012.areaSizeTreshold(counterAreaListSort);

        //System.out.println(counterAreaList.toString());
        //System.out.println(counterAreaListSort.toString());
        //System.out.println(areaThreshold);
        List<Integer> bigAreaSizeList = new ArrayList();

        for(int i = 0; i < counterAreaListSort.size(); i++)
        {
            if(counterAreaListSort.get(i) >= areaThreshold)
            {
                bigAreaSizeList.add(counterAreaListSort.get(i));
            }
        }

        List<Integer> bigAreaMarkList = new ArrayList();

        for(int i = 0; i < counterAreaList.size(); i++)
        {
            if(bigAreaSizeList.contains(counterAreaList.get(i)))
            {
                bigAreaMarkList.add(i);
            }
        }


        int counter = 0;
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {

                if(bigAreaMarkList.contains(areaMatrixReoder[i][j] + 1))
                {
                    areaMatrixReoder[i][j] = 255;
                }
                else
                {
                    areaMatrixReoder[i][j] = 0;
                }
				/*
				if(areaMatrixReoder[i][j] != 0)
				{System.out.println("mark: " + areaMatrixReoder[i][j]);}
				*/
            }
        }


        return areaMatrixReoder;
    }

    //Закрашивание мелких частей объекта
    public static int[][] getOnlyMaxPartPartBinMatrix(int[][] arrayImg, int imgWidth, int imgHeight)
    {






        int[][] areaMatrixRaw = Viy012.getAreaMatrixRaw(arrayImg, imgWidth, imgHeight);
        int[][] areaMatrixRaw2 = areaMatrixRaw.clone();

        int [][] areaMatrixReoder = Viy012.getRemarkOrderMatrix(areaMatrixRaw, imgWidth, imgHeight);
        List<Integer> markList = Viy012.getMarkList(areaMatrixReoder, imgWidth, imgHeight);
        List<Integer>counterAreaList = Viy012.getCountAreaSize(areaMatrixRaw, imgWidth, imgHeight, markList);
        //List<Integer>counterAreaListSort = Viy012.getCountAreaSizeSort(counterAreaList);
        //int areaThreshold = Viy012.areaSizeTreshold(counterAreaListSort);
        int areaThreshold = Collections.max(counterAreaList);

        //System.out.println("areaThreshold: " + areaThreshold);





        //System.out.println(counterAreaList.toString());
        //System.out.println(counterAreaListSort.toString());
        //System.out.println(areaThreshold);
        //List<Integer> bigAreaSizeList = new ArrayList();

        int mainMark = counterAreaList.indexOf(areaThreshold);

        //System.out.println("mainMakr: " + mainMark);
        //System.out.println(markList.toString());



        int counter = 0;

        //mainMark = 2;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {

                if(areaMatrixReoder[i][j] == mainMark + 1)
                {
                    areaMatrixReoder[i][j] = 0;
                    counter++;
                }
                else
                {
                    areaMatrixReoder[i][j] = 255;
                }

            }
        }

        //System.out.println("counter: " + counter);


        return areaMatrixReoder;
    }

    //Получение самой крупной части по площащи от фона
    public static int[][] getOnlyMaxPartPartBinMatrixFone(int[][] arrayImg, int imgWidth, int imgHeight)
    {
        int [][] arryaImgFone = new int [imgWidth][imgHeight];

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {

                arryaImgFone[i][j] = 0;
            }
        }



        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {

                if(arrayImg[i][j] == 255)
                {
                    arryaImgFone[i][j] = 0;

                }
                else
                {
                    arryaImgFone[i][j] = 255;
                }
            }
        }


        int[][] areaMatrixRaw = Viy012.getAreaMatrixRaw(arryaImgFone, imgWidth, imgHeight);
        int[][] areaMatrixRaw2 = areaMatrixRaw.clone();




        int [][] areaMatrixReoder = Viy012.getRemarkOrderMatrix(areaMatrixRaw, imgWidth, imgHeight);
        List<Integer> markList = Viy012.getMarkList(areaMatrixReoder, imgWidth, imgHeight);
        List<Integer>counterAreaList = Viy012.getCountAreaSize(areaMatrixRaw, imgWidth, imgHeight, markList);
        //List<Integer>counterAreaListSort = Viy012.getCountAreaSizeSort(counterAreaList);
        //int areaThreshold = Viy012.areaSizeTreshold(counterAreaListSort);
        int areaThreshold = Collections.max(counterAreaList);

        //System.out.println("areaThreshold: " + areaThreshold);





        //System.out.println(counterAreaList.toString());
        //System.out.println(counterAreaListSort.toString());
        //System.out.println(areaThreshold);
        //List<Integer> bigAreaSizeList = new ArrayList();

        int mainMark = counterAreaList.indexOf(areaThreshold);

        //System.out.println("mainMakr: " + mainMark);
        //System.out.println(markList.toString());



        int counter = 0;

        //mainMark = 2;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {

                if(areaMatrixReoder[i][j] != mainMark + 1)
                {
                    areaMatrixReoder[i][j] = 0;
                    counter++;
                }
                else
                {
                    areaMatrixReoder[i][j] = 255;
                }
            }
        }

        //System.out.println("counter: " + counter);



        return areaMatrixReoder;
    }



    //Построение выпуклой оболочки над областью двухмерного массива по заданой
    //маске его элементов
    public static int[][] getComplexConvexHull(int[][] arrayImg, int imgWidth, int imgHeight, int sizeBox)
    {
        int arrayArea[][] = new int[imgWidth][imgHeight];
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayArea[i][j] = arrayImg[i][j];

            }
        }

        int width = sizeBox,
                hight = sizeBox;

        int convexArr[][] = new int[width][hight];

        int m = imgWidth / width,
                n = imgHeight / hight;

        int size = Math.min(m, n);

        System.out.println("size: " + size);

        for(int i = 0; i < imgWidth ; i += width)
        {
            for(int j = 0; j < imgHeight; j += hight)
            {

                System.out.println("i: " + i + " | j: " + j);

                try {


                    for(int iConvex = 0; iConvex < width; iConvex++)
                    {
                        for(int jConvex = 0; jConvex < hight; jConvex++)
                        {
                            convexArr[iConvex][jConvex] = arrayArea[i+iConvex][j+jConvex];
                        }
                    }

                    convexArr = setConvexHullOnMatrix(convexArr, width, hight, 0);

                    for(int iConvex = 0; iConvex < width; iConvex++)
                    {
                        for(int jConvex = 0; jConvex < hight; jConvex++)
                        {
                            arrayArea[i+iConvex][j+jConvex] = convexArr[iConvex][jConvex];
                        }
                    }

                }
                catch(Exception ex) {}

            }
        }



        return arrayArea;
    }

    //Изменение размера изображения
    public static BufferedImage scale(BufferedImage src, int w, int h)
    {
        BufferedImage img =
                new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        int[] ys = new int[h];
        for (y = 0; y < h; y++)
            ys[y] = y * hh / h;
        for (x = 0; x < w; x++) {
            int newX = x * ww / w;
            for (y = 0; y < h; y++) {
                int col = src.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }


    public static int getAreaSize(int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {
        int areaSize = 0;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == mark)
                {
                    areaSize++;
                }
            }
        }

        return areaSize;
    }

    public static int getPerimetr(int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {
        int areaPerimetr = 0;

        int nearNeigbourhood = 0;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == mark)
                {
                    nearNeigbourhood =
                            arrayImg[i - 1][j - 1] + arrayImg[i - 1][j + 0] + arrayImg[i - 1][j + 1] +
                                    arrayImg[i + 0][j - 1] +                          arrayImg[i + 0][j + 1] +
                                    arrayImg[i + 1][j - 1] + arrayImg[i + 1][j + 0] + arrayImg[i + 1][j + 1] ;

                    if(nearNeigbourhood != mark * 8)
                    {
                        areaPerimetr++;
                    }
                }
            }
        }

        return areaPerimetr;
    }

    public static int[][] getPerimetrArray(int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {

        int areaPerimetr = 0;

        int nearNeigbourhood = 0;

        int [][] arrayPerim = new int[imgWidth][imgHeight];

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayPerim[i][j] = 255;
                if(arrayImg[i][j] == mark)
                {
                    nearNeigbourhood =
                            arrayImg[i - 1][j - 1] + arrayImg[i - 1][j + 0] + arrayImg[i - 1][j + 1] +
                                    arrayImg[i + 0][j - 1] +                          arrayImg[i + 0][j + 1] +
                                    arrayImg[i + 1][j - 1] + arrayImg[i + 1][j + 0] + arrayImg[i + 1][j + 1] ;

                    if(nearNeigbourhood != mark * 8)
                    {
                        areaPerimetr++;
                        arrayPerim[i][j] = 0;
                    }
                }
            }
        }


        //System.out.println("perim: " + areaPerimetr);


        return arrayPerim;
    }


    public static int getDeametr(int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {
        int areaSize = 0;
        List<Integer[]> pointList = new ArrayList<>();

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == mark)
                {
                    Integer[] point = new Integer[2];
                    point[0] = i;
                    point[1] = j;
                    pointList.add(point);
                }
            }
        }

        List<Integer[]> diamList = new ArrayList<>();
        int diamLength = 0;

        for(int i1 = 0; i1 < pointList.size(); i1++)
        {
            for(int i2 = 0; i2 < pointList.size(); i2++)
            {
                int
                        x1 = pointList.get(i1)[1],
                        x2 = pointList.get(i2)[1],
                        y1 = pointList.get(i1)[0],
                        y2 = pointList.get(i2)[0],
                        currDiam = 0;

                currDiam = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);

                if(diamLength < currDiam)
                {
                    diamLength = currDiam;
                    //System.out.println("curr diam max: " + diamLength);
                    diamList.clear();
                    diamList.add(pointList.get(i1));
                    diamList.add(pointList.get(i2));
                    /*
                    System.out.println("curr x1: " + diamList.get(0)[1]);
                    System.out.println("curr y1: " + diamList.get(0)[0]);
                    System.out.println("curr x2: " + diamList.get(1)[1]);
                    System.out.println("curr y2: " + diamList.get(1)[0]);
                    System.out.println();
                    */
                }

            }
        }

        int
                x1 = diamList.get(0)[1],
                y1 = diamList.get(0)[0],
                x2 = diamList.get(1)[1],
                y2 = diamList.get(1)[0],
                currDiam = 0;

        currDiam = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        currDiam = (int)Math.sqrt(currDiam);
        //System.out.println("diam: " + currDiam);





        return currDiam;
    }

    public static int[][] getDiametrArraay(int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {
        int areaSize = 0;
        List<Integer[]> pointList = new ArrayList<>();

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == mark)
                {
                    Integer[] point = new Integer[2];
                    point[0] = i;
                    point[1] = j;
                    pointList.add(point);
                }
            }
        }

        List<Integer[]> diamList = new ArrayList<>();
        int diamLength = 0;

        for(int i1 = 0; i1 < pointList.size(); i1++)
        {
            for(int i2 = 0; i2 < pointList.size(); i2++)
            {
                int
                        x1 = pointList.get(i1)[1],
                        x2 = pointList.get(i2)[1],
                        y1 = pointList.get(i1)[0],
                        y2 = pointList.get(i2)[0],
                        currDiam = 0;

                currDiam = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);

                if(diamLength < currDiam)
                {
                    diamLength = currDiam;
                    //System.out.println("curr diam max: " + diamLength);
                    diamList.clear();
                    diamList.add(pointList.get(i1));
                    diamList.add(pointList.get(i2));
                    /*
                    System.out.println("curr x1: " + diamList.get(0)[1]);
                    System.out.println("curr y1: " + diamList.get(0)[0]);
                    System.out.println("curr x2: " + diamList.get(1)[1]);
                    System.out.println("curr y2: " + diamList.get(1)[0]);
                    System.out.println();
                    */
                }

            }
        }

        int
                x1 = diamList.get(0)[1],
                y1 = diamList.get(0)[0],
                x2 = diamList.get(1)[1],
                y2 = diamList.get(1)[0],
                currDiam = 0;

        //int [] coordArray = {10, 10, 150, 150};
        int [] coordArray = {y1, x1, y2, x2};
        /*
        currDiam = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        currDiam = (int)Math.sqrt(currDiam);
        System.out.println("diam: " + currDiam);
        */
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayImg[i][j] = 255;
            }
        }

        arrayImg = setLineOnMatrix(arrayImg, imgWidth, imgHeight, coordArray, mark);

        /*
        int counter = 0;
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == mark)
                {
                    counter++;
                }
            }
        }
        */

        //System.out.println("counter diam: " + counter);

        return arrayImg;
    }


    public static int[][] getConvexHullArraay(int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {

        int counter = 0;
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == 0)
                {
                    counter++;
                }
            }
        }

        Point[] p = new Point[counter];
        counter = 0;
        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == 0)
                {
                    p[counter] = new Point();
                    p[counter].x = j;
                    p[counter].y = i;

                    counter++;
                }
            }
        }


        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayImg[i][j] = 255;

            }
        }


        Point[] hull = ConvexHull.convex_hull(p).clone();

        for (int i = 0; i < hull.length; i++) {
            if (hull[i] != null)
            {
                arrayImg[hull[i].y][hull[i].x] = 0;
            }
        }

        for (int i = 0; i < hull.length-1; i++) {
            if (hull[i] != null)
            {
                int[] coordArr = {hull[i].y, hull[i].x, hull[i+1].y, hull[i+1].x};
                arrayImg = Viy012.setLineOnMatrix(arrayImg, imgWidth, imgHeight, coordArr, 0);
            }
        }
        int[] coordArr = {hull[hull.length-1].y, hull[hull.length-1].x, hull[0].y, hull[0].x};
        arrayImg = Viy012.setLineOnMatrix(arrayImg, imgWidth, imgHeight, coordArr, 0);

        for(int i = 0; i < imgWidth; i++)
        {
            List<Integer[]> line = new ArrayList<>();
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == 0)
                {
                    Integer[] coordArrPoint = new Integer[2];
                    coordArrPoint[0] = i;
                    coordArrPoint[1] = j;
                    line.add(coordArrPoint);
                }

            }
            if(line.size() > 0)
            {
                for(int j1 = line.get(0)[1]; j1 < line.get(line.size()-1)[1]; j1++)
                {
                    arrayImg[line.get(0)[0]][j1] = 0;
                }

            }
        }

        return arrayImg;

    }


    public static int[] getCenterCoordArraay(int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {
        int [] centerCoord = new int [2];

        int
                xSum = 0,
                ySum = 0,
                count = 0;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == mark)
                {
                    xSum += j;
                    ySum += i;
                    count++;
                }
            }
        }

        centerCoord[0] = (int)((float)(ySum) / (float)(count));
        centerCoord[1] = (int)((float)(xSum) / (float)(count));

        return centerCoord;
    }

    public static float getAssymetric(int[][] arrayImg, int imgWidth, int imgHeight, int[] centerCoord, int mark)
    {

        List<Float> radiusList = new ArrayList<>();
        List<Float> counterList = new ArrayList<>();

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                if(arrayImg[i][j] == mark)
                {
                    int
                            x1 = centerCoord[1],
                            y1 = centerCoord[0],
                            x2 = j,
                            y2 = i;

                    float radius = 0.0f;

                    radius = (float) Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));

                    if(!radiusList.contains(radius))
                    {
                        radiusList.add(radius);
                        counterList.add(1.0f);
                    }

                    if(radiusList.contains(radius))
                    {
                        int ind = radiusList.indexOf(radius);
                        counterList.set(ind, counterList.get(ind) + 1.0f);
                    }
                }
            }
        }

        float counterSumFloat = 0.0f;
        for(int i = 0; i < counterList.size(); i++)
        {
            counterSumFloat+=counterList.get(i);
        }

        for(int i = 0; i < counterList.size(); i++)
        {
            counterList.set(i, counterList.get(i)/counterSumFloat);
        }

        float
                matOgid = 0.0f,
                v2 = 0.0f,
                v3 = 0.0f,
                sredne = 0.0f,
                sigm = 0.0f,
                m3 = 0.0f,
                asym = 0.0f;

        for(int i = 0; i < radiusList.size(); i++)
        {
            matOgid += radiusList.get(i) * counterList.get(i);
        }

        for(int i = 0; i < radiusList.size(); i++)
        {
            v2 += radiusList.get(i) * radiusList.get(i) * counterList.get(i);
        }

        for(int i = 0; i < radiusList.size(); i++)
        {
            v3 += radiusList.get(i) * radiusList.get(i) * radiusList.get(i) * counterList.get(i);
        }

        /*
        for(int i = 0; i < radiusList.size(); i++)
        {
            sredne += radiusList.get(i);
        }

        sredne = sredne / radiusList.size();

        for(int i = 0; i < radiusList.size(); i++)
        {
            sigm += (radiusList.get(i) - sredne) * (radiusList.get(i) - sredne);
        }

        sigm = sigm / radiusList.size();

        sigm = (float) Math.sqrt(sigm);
        */

        sigm = v2 - matOgid * matOgid;
        sigm = (float) Math.sqrt(sigm);

        m3 = v3 - 3.0f * v2 * matOgid + 2.0f * matOgid * matOgid * matOgid;

        asym = m3 / (sigm * sigm * sigm);

        asym = (float) new BigDecimal(asym).setScale(2, RoundingMode.UP).doubleValue();

        asym = Math.abs(asym);

        return asym;
    }

    public static boolean[] getColorArray(BufferedImage img, int[][] arrayImg, int imgWidth, int imgHeight, int mark)
    {
        imgWidth = imgWidth - 2;
        imgHeight = imgHeight - 2;

        int[][] arrayImg2 = new int[imgWidth][imgHeight];

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {
                arrayImg2[i][j] = arrayImg[i + 1][j + 1];
            }
        }

        boolean colorArr[] = new boolean[6];


        //int grayscaleMatrix[][] = new int[imgWidth][imgHeight];

        int
                r = 0,
                g = 0,
                b = 0,
                aver = 0;

        for(int i = 0; i < imgWidth; i++)
        {
            for(int j = 0; j < imgHeight; j++)
            {

                if(arrayImg2[i][j] == mark) {

                    Color clr = new Color(img.getRGB(i, j));
                    r = clr.getRed();
                    g = clr.getGreen();
                    b = clr.getBlue();

                    boolean bColorBlack = r < 35 && g < 35 && b < 35,
                            bColorWhite = r > 200 && g > 200 && b > 200,
                            bColorRed = r > g + b && r > 150,
                            bColorGreen = g < r + b && g > 150,
                            bColorBlue = b < r + g && b > 150,
                            bColorBordov = (10 * r < (b + g) * 11) && (10 * r > (b + g) * 9);

                    if(bColorBlack)
                    {
                        colorArr[0] = true;
                    }

                    if(bColorWhite)
                    {
                        colorArr[1] = true;
                    }

                    if(!bColorBlack && !bColorWhite && bColorRed)
                    {
                        colorArr[2] = true;
                    }

                    if(!bColorBlack && !bColorWhite && bColorBlue)
                    {
                        colorArr[3] = true;
                    }

                    if(!bColorBlack && !bColorWhite && bColorGreen)
                    {
                        colorArr[4] = true;
                    }

                    if(!bColorBlack && !bColorWhite && bColorBordov)
                    {
                        colorArr[5] = true;
                    }

                }
                //grayscaleMatrix[i][j] = aver;

            }
        }

        return colorArr;

    }

    public static void imageSavePathParam(BufferedImage img,
                                   int[][] arrayArea,
                                   int[][] arrayPerimm,
                                   int[][] arrayDiametr,
                                   int[][] arrayConvexHull, int imgWidth, int imgHeight, String path, int mark) throws Exception
    {

        for(int i=0;i<img.getWidth();i++)
        {
            for(int j=0;j<img.getHeight();j++)
            {
                Color clr = new Color(img.getRGB(i, j));
                int r = clr.getRed();
                int g = clr.getGreen();
                int b = clr.getBlue();




                if(arrayArea[i][j] == mark)
                {
                    clr = new Color(0, g, b);

                    img.setRGB(i, j, clr.getRGB());
                }

                if(arrayPerimm[i][j] == mark)
                {
                    clr = new Color(0, 0, 255);

                    img.setRGB(i, j, clr.getRGB());
                }

                if(arrayDiametr[i][j] == mark)
                {
                    clr = new Color(255, 0, 0);

                    img.setRGB(i, j, clr.getRGB());
                }

                if(arrayArea[i][j] == 255 && arrayConvexHull[i][j] == mark)
                {
                    clr = new Color(r , g / 2, b / 4);

                    img.setRGB(i, j, clr.getRGB());
                }


            }
        }


        ImageIO.write(img, "png", new File(path));
    }




}