package com.proj242.proj242.imgproj;

import java.awt.image.BufferedImage;
import java.util.List;

//класс данных изображения
public class ImgData
{
    //Само изображение
    public BufferedImage img;

    //Ширина изображения
    public int imgWidth;

    //Высота изображения
    public int imgHeight;

    //Матрица уровней градаций серого
    public int grayscaleMatrix[][];

    //Матрица бинаризированного изображения значений 0 | 255
    public int binMatrix[][];

    //Матрица метода Соболева
    public int methodSobolevaMatrix[][];

    //Матрица отдельных не связаных участков изображения в сыром виде
    public int areaMatrixRaw[][];

    //Число отдельных участков бинаризированного изображения до очистки
    public int areaCounterRaw;

    //Матрица отдельных не связанных участков изображения
    //с переопределенными положительными масками и фоном, равном 0
    public int areaMatrixReoder[][];

    //Список масок отдельных не связанных участков бинаризированного изображения
    public List<Integer> markList;

    //Список матриц отдельных не связанных участкок бинаризированного изображения
    public List<int[][]> matrixMarAreakList;

    //Список размеров отдельных не связанных участков бинаризированного изображения
    public List<Integer> counterAreaList;

    //Список сортированных размеров отдельных не связанных участков бинаризированного изображения
    public List<Integer> sortCounterAreakList;

    //Список индексов размеров отдельных не связанных участков бинаризированного изображения
    //в сортированном списке масок этих участков относительно несортированного
    //public List<Integer> countAreaIndexSortFromUnsortList;

    //Порог размера отдельных несвязанных между собой участков изображения
    //разбивающих области на мусоор и объекты
    public int areaSizeTreshold;

    public int[][] clearBinMat;

    public int[][] getClearBinMat() {

        int[][] clearBinMatCopy = new int[this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++) {
            for (int j = 0; j < this.imgHeight; j++)
            {
                clearBinMatCopy[i][j] = this.clearBinMat[i][j];
            }
        }

        return clearBinMatCopy;
    }

    public void setClearBinMat(int[][] clearBinMat) {

        this.imgWidth = this.imgWidth + 2;
        this.imgHeight = this.imgHeight + 2;
        this.clearBinMat = new int[this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++) {
            for (int j = 0; j < this.imgHeight; j++)
            {
                this.clearBinMat[i][j] = 255;
                //this.clearBinMat[i][j] = clearBinMat[i][j];
            }
        }
        for(int i = 1; i < this.imgWidth - 1; i++) {
            for (int j = 1; j < this.imgHeight - 1; j++)
            {
                //this.clearBinMat[i][j] = 255;
                this.clearBinMat[i][j] = clearBinMat[i - 1][j - 1];
            }
        }




    }

    public void getDataFromImg(BufferedImage img)
    {
        this.imgWidth = Viy012.getImgWidth(img);
        this.imgHeight = Viy012.getImgHeight(img);

        this.grayscaleMatrix = Viy012.getGrayscaleMatrixDesaturation(img, this.imgWidth, this.imgHeight);

        this.binMatrix = Viy012.getBinMatrixMethodOtsu(this.grayscaleMatrix, this.imgWidth, this.imgHeight);
        //this.binMatrix = Viy012.getBinMatrixMethodGlobalThreshold(this.grayscaleMatrix, this.imgWidth, this.imgHeight);

        int[][] areaMatrixBinClear = Viy012.getOnlyMaxPartPartBinMatrix(this.binMatrix, this.imgWidth, this.imgHeight);
        int[][] areaMatrixBinClearFone = Viy012.getOnlyMaxPartPartBinMatrixFone(areaMatrixBinClear, this.imgWidth, this.imgHeight);

        this.setClearBinMat(areaMatrixBinClearFone);

        try {
            //Viy012.imageSavePath(this.getClearBinMat(), this.imgWidth, this.imgHeight, "d:\\imgsal\\result\\area.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Конструктор по умолчанию
    public ImgData()
    {

    }

    public int area;
    public int[][] areaMatrix;

    public int perimetr;
    public int [][] perimetrMatrix;

    public int diametr;
    public int [][] diametrMatrix;

    public float bound;
    public int [][] boundMatrix;

    public int[][] getAreaMatrix() {

        int [][] areaMatrix = new int [this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++)
        {
            for(int j = 0; j < this.imgHeight; j++)
            {
                areaMatrix[i][j] = this.areaMatrix[i][j];
            }
        }


        return areaMatrix;
    }

    public void setAreaMatrix(int[][] areaMatrix) {

        this.areaMatrix = new int [this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++)
        {
            for(int j = 0; j < this.imgHeight; j++)
            {
                this.areaMatrix[i][j] = areaMatrix[i][j];
            }
        }
    }

    public int[][] getPerimetrMatrix() {

        int [][] perimetrMatrix = new int [this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++)
        {
            for(int j = 0; j < this.imgHeight; j++)
            {
                perimetrMatrix[i][j] = this.perimetrMatrix[i][j];
            }
        }


        return perimetrMatrix;
    }

    public void setPerimetrMatrix(int[][] perimetrMatrix) {
        this.perimetrMatrix = new int [this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++)
        {
            for(int j = 0; j < this.imgHeight; j++)
            {
                this.perimetrMatrix[i][j] = perimetrMatrix[i][j];
            }
        }
    }

    public int[][] getDiametrMatrix() {

        int [][] diametrMatrix = new int [this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++)
        {
            for(int j = 0; j < this.imgHeight; j++)
            {
                diametrMatrix[i][j] = this.diametrMatrix[i][j];
            }
        }


        return diametrMatrix;
    }

    public void setDiametrMatrix(int[][] diametrMatrix) {
        this.diametrMatrix = new int [this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++)
        {
            for(int j = 0; j < this.imgHeight; j++)
            {
                this.diametrMatrix[i][j] = diametrMatrix[i][j];
            }
        }
    }

    public int[][] getBoundMatrix() {
        int [][] boundMatrix = new int [this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++)
        {
            for(int j = 0; j < this.imgHeight; j++)
            {
                boundMatrix[i][j] = this.boundMatrix[i][j];
            }
        }

        return boundMatrix;
    }

    public void setBoundMatrix(int[][] boundMatrix) {
        this.boundMatrix = new int [this.imgWidth][this.imgHeight];

        for(int i = 0; i < this.imgWidth; i++)
        {
            for(int j = 0; j < this.imgHeight; j++)
            {
                this.boundMatrix[i][j] = boundMatrix[i][j];
            }
        }
    }

    int [] centerCoord;

    public int[] getCenterCoord() {

        int[] centerCoord = new int[2];

        centerCoord[0] = this.centerCoord[0];
        centerCoord[1] = this.centerCoord[1];

        return centerCoord;
    }

    public void setCenterCoord(int[] centerCoord) {

        this.centerCoord = new int[2];

        this.centerCoord[0] = centerCoord[0];
        this.centerCoord[1] = centerCoord[1];
    }

    public float assymetric;

    public boolean[] colorArr;
}

