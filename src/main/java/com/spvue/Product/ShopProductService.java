package com.spvue.Product;

import com.spvue.Image.ImageService;
import com.spvue.Member.Member;
import com.spvue.Product.DTO.*;
import com.spvue.Product.Subs.Benefit;
import com.spvue.Product.Subs.Discount;
import com.spvue.Product.Subs.Option;
import com.spvue.Product.Subs.Repository.BenefitRepository;
import com.spvue.Product.Subs.Repository.DiscountRepository;
import com.spvue.Product.Subs.Repository.OptionRepository;
import com.spvue.Product.Subs.Repository.ShippingRepository;
import com.spvue.Product.Subs.Shipping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopProductService {
    private final ShopProductRepository productRepository;
    private final ImageService imageService;
    private final OptionRepository optionRepository;
    private final BenefitRepository benefitRepository;
    private final ShippingRepository shippingRepository;
    private final DiscountRepository discountRepository;


    public ShopProduct save(ProductRequestDTO productDTO, Member author) {
        // 1. Product 엔티티로 변환
        ShopProduct product = new ShopProduct();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setFileURLs(productDTO.getFileURLs());
        product.setShipping(productDTO.getShipping());
        product.setManufacturer(productDTO.getManufacturer());
        product.setCondition(productDTO.getCondition());
        product.setAuthor(author);


        List<String> URLs = product.getFileURLs();
        for (String imgURL : URLs) {
            imageService.imageFinalSave(imgURL);  // 각 URL에 대해 imageFinalSave 호출
        }

        product = productRepository.save(product);

        // 2. Option, Benefit, Shipping 등 연관 엔티티를 저장
        // 예: product.setOptions(mapOptionDTOs(productDTO.getOptions()));
        // 3. 연관된 엔티티들 저장
        List<Option> options = mapOptionDTOs(productDTO.getOptions(), product);
        optionRepository.saveAll(options);

        List<Benefit> benefits = mapBenefitDTOs(productDTO.getBenefits(), product);
        benefitRepository.saveAll(benefits);

        List<Shipping> shipping = mapShippingDTOs(productDTO.getShippingInfo(), product);
        shippingRepository.saveAll(shipping);

        List<Discount> discounts = mapDiscountDTOs(productDTO.getDiscounts(), product);
        discountRepository.saveAll(discounts);

        return product;
    }


    private List<Shipping> mapShippingDTOs(List<ShippingDTO> shippingDTOs, ShopProduct product) {
        return shippingDTOs.stream().map(shippingDTO -> {
            Shipping shipping = new Shipping();
            shipping.setType(shippingDTO.getType());
            shipping.setFreeShipping(shippingDTO.getFreeShipping());
            shipping.setCourier(shippingDTO.getCourier());
            shipping.setIslandAdditionalFee(shippingDTO.getIslandAdditionalFee());
            shipping.setShopProduct(product); // 연관관계 설정 (ManyToOne 또는 OneToMany에 맞게 설정)
            return shipping;
        }).collect(Collectors.toList());
    }


    private List<Option> mapOptionDTOs(List<OptionDTO> optionDTOs, ShopProduct product) {
        return optionDTOs.stream().map(optionDTO -> {
            Option option = new Option();
            option.setSize(optionDTO.getSize());
            option.setColor(optionDTO.getColor());
            option.setShopProduct(product); // 연관관계 설정 (ManyToOne)
            return option;
        }).collect(Collectors.toList());
    }

    private List<Discount> mapDiscountDTOs(List<DiscountDTO> discountDTOs, ShopProduct product) {
        return discountDTOs.stream().map(discountDTO -> {
            Discount discount = new Discount();
            discount.setGrade(discountDTO.getGrade());
            discount.setDiscountRate(discountDTO.getDiscountRate());
            discount.setShopProduct(product); // 연관관계 설정 (ManyToOne)
            return discount;
        }).collect(Collectors.toList());
    }

    private List<Benefit> mapBenefitDTOs(List<BenefitDTO> benefitDTOs, ShopProduct product) {
        return benefitDTOs.stream().map(benefitDTO -> {
            Benefit benefit = new Benefit();
            benefit.setDescription(benefitDTO.getDescription());
            benefit.setAmount(benefitDTO.getAmount());
            benefit.setShopProduct(product); // 연관관계 설정 (ManyToOne)
            return benefit;
        }).collect(Collectors.toList());
    }


    public ShopProduct findById(Long id) {

        ShopProduct product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Value not found!"));
        // 빈 Optional의 경우, 예외를 던짐
        Optional<String> emptyOptional = Optional.empty();
        try {
            String emptyValue = emptyOptional.orElseThrow(() -> new RuntimeException("Value not found!"));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage()); // 출력: Value not found!
        }
        return product;
    }
}
