package com.aleengo.iconvert.persistence.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aleengo.iconvert.domain.model.CurrencyEntity;
import com.aleengo.iconvert.persistence.model.db.CurrencyData;
import com.aleengo.iconvert.persistence.model.db.FavoriteData;
import com.aleengo.iconvert.persistence.model.db.RateData;

import java.util.List;

/**
 * Created by CK_ALEENGO on 25/02/2019.
 * Copyright (c) 2019. All rights reserved.
 */
@Dao
public interface ConvertDAO {

    // rates
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAllRates(List<RateData> rates);

    @Query("DELETE FROM rate")
    void deleteAllRates();

    // currencies
    @Query("SELECT c.code, c.libelle, r.rate as value FROM currency c " +
            "INNER JOIN rate r ON c.code = r.code")
    List<CurrencyEntity> getAllCurrencies();

    @Query("SELECT c.code, c.libelle, r.rate as value FROM currency c " +
            "INNER JOIN rate r ON r.code = c.code " +
            "WHERE c.code = :code")
    CurrencyEntity getCurrency(String code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAllCurrencies(List<CurrencyData> currencies);

    @Query("DELETE FROM currency")
    void deleteAllCurrencies();

    // Favorites
    @Query("SELECT * from favorite")
    List<FavoriteData> getFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveFavorite(FavoriteData currency);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveFavorites(List<FavoriteData> favorites);

    @Query("DELETE FROM favorite WHERE dest_code = :code")
    void deleteFavorite(String code);

    @Query("DELETE FROM favorite")
    void deleteAllFavorites();
}
