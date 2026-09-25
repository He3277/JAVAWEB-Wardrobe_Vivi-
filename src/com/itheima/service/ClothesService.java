package com.itheima.service;

import com.itheima.dao.ClothesDao;
import com.itheima.model.Clothes;
import com.itheima.model.Type;

import java.util.List;

public class ClothesService {
    ClothesDao clothesDao = new ClothesDao();

    public List<Clothes> getAllClothesData() {
        try {
            List<Clothes> clothesList = clothesDao.getClothesByParams(null, null, null);
            for (Clothes clothes : clothesList) {
                List sizeList = clothesDao.getSizeByType(clothes.getTypeId());
                clothes.setSizeList(sizeList);
            }
            return clothesList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String addClothes(Clothes clothes) {
        try {
            int count = clothesDao.addClothes(clothes);
            if (count > 0) {
                return "上架成功";
            }
            return "上架失败";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Clothes> getClothesByParams(String clothName, String style, String typeName) {
        try {
            List<Clothes> clothesList = clothesDao.getClothesByParams(clothName, style, typeName);
            for (Clothes clothes : clothesList) {
                List sizeList = clothesDao.getSizeByType(clothes.getTypeId());
                clothes.setSizeList(sizeList);
            }
            return clothesList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String delClothes(int id) {
        try {
            int count = clothesDao.deleteClothes(id);
            if (count > 0) {
                return "下架服装成功！";
            }
            return "下架失败！";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String editClothes(Clothes clothes) {
        try {
            int count = clothesDao.editClothes(clothes);
            if (count > 0) {
                return "修改成功！";
            }
            return "修改失败！";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Clothes getClothDetails(int id) {
        try {
            Clothes clothes = clothesDao.getClothById(id);
            return clothes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Clothes> getClothesByName(String clothesName) {
        try {
            List<Clothes> clothesList = clothesDao.getClothesByName(clothesName);
            return clothesList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Clothes> getAllClothes(String style, String typeName) {
        try {
            return clothesDao.getAllClothes(style, typeName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllStyles() {
        try {
            return clothesDao.getStyles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Type> getAllTypes() {
        try {
            return clothesDao.getTypes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
