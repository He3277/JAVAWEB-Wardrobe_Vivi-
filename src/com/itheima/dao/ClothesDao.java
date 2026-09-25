package com.itheima.dao;

import com.itheima.model.Clothes;
import com.itheima.model.Size;
import com.itheima.model.Type;
import com.itheima.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClothesDao {

    public int deleteClothes(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "DELETE FROM t_clothes WHERE id=?";
        return qr.update(sql, id);
    }

    public int editClothes(Clothes clothes) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "UPDATE t_clothes SET cloth_name = ?, image = ?, type_id = ?, style = ?, price = ? WHERE id = ?";
        Object[] params = {clothes.getClothName(), clothes.getImage(), clothes.getTypeId(), clothes.getStyle(), clothes.getPrice(), clothes.getId()};
        return qr.update(sql, params);
    }

    public List<Clothes> getClothesByParams(String clothName, String style, String typeName) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        StringBuilder sql = new StringBuilder("SELECT t_clothes.id, cloth_name AS clothName,image, type_id AS typeId, type_name AS typeName, style, price FROM t_clothes JOIN t_type ON type_id = t_type.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (clothName != null && !clothName.isEmpty()) {
            sql.append(" AND cloth_name LIKE ?");
            params.add("%" + clothName + "%");
        }
        if (style != null && !style.isEmpty()) {
            sql.append(" AND style = ?");
            params.add(style);
        }
        if (typeName != null && !typeName.isEmpty()) {
            sql.append(" AND type_name = ?");
            params.add(typeName);
        }

        return qr.query(sql.toString(), new BeanListHandler<>(Clothes.class), params.toArray());
    }

    public int addClothes(Clothes clothes) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "INSERT INTO t_clothes (`cloth_name`, image, type_id,style, price) VALUES (?,?,?,?,?)";
        Object[] params = {clothes.getClothName(), clothes.getImage(), clothes.getTypeId(), clothes.getStyle(), clothes.getPrice()};
        return qr.update(sql, params);
    }

    public List<Size> getSizeByType(int typeId) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT t_size.id, size_name AS sizeName FROM t_size JOIN t_type ON t_size.type_id = t_type.id WHERE t_size.type_id = ?";
        List<Size> sizeList = qr.query(sql, new BeanListHandler<>(Size.class), typeId);
        return sizeList;
    }

    public Clothes getClothById(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT t_clothes.id,cloth_name AS clothName ,image,t_type.id AS typeId, image, t_type.type_name AS typeName, t_clothes.style, t_clothes.price FROM t_clothes JOIN t_type ON t_clothes.type_id = t_type.id WHERE t_clothes.id = ?";
        Clothes clothes = qr.query(sql, new BeanHandler<>(Clothes.class), id);
        int typeId = clothes.getTypeId();
        List<Size> sizeList = getSizeByType(typeId);
        clothes.setSizeList(sizeList);
        return clothes;
    }

    public List<Clothes> getClothesByName(String clothesName) {
        try {
            QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
            String sql = "SELECT t_clothes.id, cloth_name AS clothName, image, type_name AS typeName, style, price FROM t_clothes JOIN t_type ON t_clothes.type_id = t_type.id WHERE cloth_name LIKE ?";
            List<Clothes> clothesList = qr.query(sql, new BeanListHandler<>(Clothes.class), "%" + clothesName + "%");
            return clothesList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getStyles() throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT style FROM t_clothes GROUP BY style";
        List<String> styleList = qr.query(sql, new ColumnListHandler<>());
        return styleList;
    }

    public List<Type> getTypes() throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, type_name AS typeName FROM t_type";
        List<Type> typeList = qr.query(sql, new BeanListHandler<>(Type.class));
        return typeList;
    }

    public List<Clothes> getAllClothes(String style, String typeName) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        StringBuilder sql = new StringBuilder("SELECT t_clothes.id, cloth_name AS clothName, image, type_name AS typeName, style, price FROM t_clothes JOIN t_type ON t_clothes.type_id = t_type.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (style != null && !style.isEmpty()) {
            sql.append(" AND style=?");
            params.add(style);
        }
        if (typeName != null && !typeName.isEmpty()) {
            sql.append(" AND type_name =?");
            params.add(typeName);
        }

        return qr.query(sql.toString(), new BeanListHandler<>(Clothes.class), params.toArray());
    }
}
