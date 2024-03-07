package com.yunhongmin.codi.initialize;

import com.yunhongmin.codi.domain.CodiBrand;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiCategoryStat;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.repository.CodiBrandRepository;
import com.yunhongmin.codi.repository.CodiCategoryStatRepository;
import com.yunhongmin.codi.repository.CodiProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CodiDataInitializer implements CommandLineRunner {
    private final CodiBrandRepository codiBrandRepository;
    private final CodiProductRepository codiProductRepository;
    private final CodiCategoryStatRepository codiCategoryStatRepository;

    @Override
    public void run(String... args) throws Exception {
        // data initialize codi brand and product
        CodiBrandData[] codiBrandDatas = new CodiBrandData[]{
                new CodiBrandData("A", new int[]{11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300}),
                new CodiBrandData("B", new int[]{10500, 5900, 3800, 9100, 2100, 2000, 2000, 2200}),
                new CodiBrandData("C", new int[]{10000, 6200, 3300, 9200, 2200, 1900, 2200, 2100}),
                new CodiBrandData("D", new int[]{10100, 5100, 3000, 9500, 2500, 1500, 2400, 2000}),
                new CodiBrandData("E", new int[]{10700, 5000, 3800, 9900, 2300, 1800, 2100, 2100}),
                new CodiBrandData("F", new int[]{11200, 7200, 4000, 9300, 2100, 1600, 2300, 1900}),
                new CodiBrandData("G", new int[]{10500, 5800, 3900, 9000, 2200, 1700, 2100, 2000}),
                new CodiBrandData("H", new int[]{10800, 6300, 3100, 9700, 2100, 1600, 2000, 2000}),
                new CodiBrandData("I", new int[]{11400, 6700, 3200, 9500, 2400, 1700, 1700, 2400})
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

        // data initialize for min price by category
        List<CodiCategoryStat> codiCategoryStatMins = codiProductRepository.findCodiCategoryStatMin();
        for (CodiCategoryStat codiCategoryStatMin : codiCategoryStatMins) {
            codiCategoryStatRepository.save(codiCategoryStatMin);
        }

        // data initialize for max price by category
        List<CodiCategoryStat> codiCategoryStatMaxs = codiProductRepository.findCodiCategoryStatMax();
        for (CodiCategoryStat codiCategoryStatMax : codiCategoryStatMaxs) {
            codiCategoryStatRepository.save(codiCategoryStatMax);
        }


    }
}
