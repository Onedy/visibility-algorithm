package com.inditex.visibilityalgorithm.core.csv;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ProductVisibility {
    public static final int BACK_SOON = 2;
    public static final int PRODUCT_ID = 1;
    public static final int ID = 0;
    public static final int SIZE_ID = 0;
    public static final int SEQUENCE = 1;
    public static final int SPECIAL = 3;
    public static final int QUANTITY = 1;

    @Getter
    @Builder
    private static class Product implements Comparable<Product> {
        private String id;
        private int sequence;
        @Setter
        private List<ProductVisibility.Size> sizes;

        @Override
        public int compareTo(Product other) {
            return sequence - other.sequence;
        }
    }

    @Getter
    @Builder
    private static class Size {
        private String id;
        private String productId;
        private String backSoon;
        private String special;
        @Setter
        private int quantity;
    }

    public static String visibleIds(List<String[]> productCsv, List<String[]> sizeCsv, List<String[]> stockCsv) {
        Map<String, Size> allSizes = readSizes(sizeCsv);

        readStocks(stockCsv, allSizes);

        Map<String, Product> allProducts = readProducts(productCsv);

        matchSizesToProducts(allSizes, allProducts);

        return filterVisibleProductIds(allProducts.values()).stream().map(Product::getId).collect(Collectors.joining(","));
    }

    private static Set<Product> filterVisibleProductIds(Collection<Product> products) {
        Set<Product> visibleProductIds = new TreeSet<>();
        products.forEach(product -> {
            if (shouldBeVisible(product)) {
                visibleProductIds.add(product);
            }
        });
        return visibleProductIds;
    }

    private static boolean shouldBeVisible(Product product) {
        if (hasASpecialSize(product)) {
            return hasASpecialAndVisibleSize(product) && hasANotSpecialAndVisibleSize(product);
        } else {
            return hasAVisibleSize(product);
        }
    }

    private static boolean hasAVisibleSize(Product product) {
        return product.getSizes().stream().anyMatch(size -> isVisible(size));
    }

    private static boolean isVisible(Size size) {
        return isBackSoon(size) || size.getQuantity() > 0;
    }

    private static boolean hasASpecialSize(Product product) {
        return product.getSizes().stream().anyMatch(ProductVisibility::isSpecial);
    }

    private static boolean hasANotSpecialAndVisibleSize(Product product) {
        return product.getSizes().stream().anyMatch(size -> isNotSpecialAndVisible(size));
    }

    private static boolean hasASpecialAndVisibleSize(Product product) {
        return product.getSizes().stream().anyMatch(size -> isSpecialAndVisible(size));
    }

    private static boolean isSpecialAndVisible(Size size) {
        return isSpecial(size) && isVisible(size);
    }

    private static boolean isSpecial(Size size) {
        return "true".equals(size.getSpecial());
    }

    private static boolean isBackSoon(Size size) {
        return "true".equals(size.getBackSoon());
    }

    private static boolean isNotSpecialAndVisible(Size size) {
        return !isSpecial(size) && isVisible(size);
    }

    private static void matchSizesToProducts(Map<String, Size> allSizes, Map<String, Product> allProducts) {
        for (Size size : allSizes.values()) {
            allProducts.get(size.getProductId()).getSizes().add(size);
        }
    }

    private static Map<String, Product> readProducts(List<String[]> productCsv) {
        Map<String, Product> allProducts = new HashMap<>();
        for (String[] product : productCsv) {
            String productId = product[ID];
            allProducts.put(productId, Product.builder()
                .id(productId)
                .sequence(Integer.parseInt(product[SEQUENCE].trim()))
                .sizes(new ArrayList<>())
                .build());
        }
        return allProducts;
    }

    private static void readStocks(List<String[]> stockCsv, Map<String, Size> allSizes) {
        for (String[] stock : stockCsv) {
            String sizeId = stock[SIZE_ID];
            allSizes.get(sizeId).setQuantity(Integer.parseInt(stock[QUANTITY].trim()));
        }
    }

    private static Map<String, Size> readSizes(List<String[]> sizeCsv) {
        Map<String, Size> allSizes = new HashMap<>();
        for (String[] size : sizeCsv) {
            String sizeId = size[ID].trim();
            allSizes.put(sizeId, Size.builder()
                .id(sizeId)
                .backSoon(size[BACK_SOON].trim())
                .special(size[SPECIAL].trim())
                .productId(size[PRODUCT_ID].trim())
                .build());
        }
        return allSizes;
    }
}
