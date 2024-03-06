package com.yunhongmin.codi.initialize;

import com.yunhongmin.codi.domain.CodiBrand;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.repository.CodiBrandRepository;
import com.yunhongmin.codi.repository.CodiProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final CodiBrandRepository codiBrandRepository;
    private final CodiProductRepository codiProductRepository;

    @Override
    public void run(String... args) throws Exception {
        CodiBrandData[] codiBrandDatas = new CodiBrandData[]{
                new CodiBrandData("A", new int[]{11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300})
        };

        for (CodiBrandData codiBrandData : codiBrandDatas) {
            CodiBrand codiBrand = new CodiBrand();
            codiBrand.setName(codiBrandData.getBrandName());
            codiBrand.setTotalPrice(codiBrandData.getTotalPrice());
            codiBrandRepository.save(codiBrand);

            int[] prices = codiBrandData.getPrices();

            for (int i = 0; i < prices.length; i++) {
                CodiProduct codiProduct = new CodiProduct();
                codiProduct.setCodiBrand(codiBrand);
                codiProduct.setCodiCategory(CodiCategory.values()[i]);
                codiProduct.setPrice(prices[i]);
                codiProductRepository.save(codiProduct);
            }
        }
    }
}
