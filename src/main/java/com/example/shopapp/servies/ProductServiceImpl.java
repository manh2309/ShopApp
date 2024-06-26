package com.example.shopapp.servies;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repository.CategoryRepository;
import com.example.shopapp.repository.ProductImageRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category exitsingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find category"));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbNail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(exitsingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id) throws DataNotFoundException {

        Product product = productRepository.findById(id).
        orElseThrow(() -> new DataNotFoundException("Cannot found id"));
        return product;
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        //lấy danh sách sản phẩm  theo trang(page) và giới hạn(limit)
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
            Product exitstingProduct = getProductById(id);
            if(exitstingProduct != null){
            Category exitsingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find category"));
                exitstingProduct.setName(productDTO.getName());
                exitstingProduct.setCategory(exitsingCategory);
                exitstingProduct.setPrice(productDTO.getPrice());
                exitstingProduct.setDescription(productDTO.getDescription());
                exitstingProduct.setThumbNail(productDTO.getThumbnail());
                return productRepository.save(exitstingProduct);
            }
          return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> product= productRepository.findById(id);
        product.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
            Product exitstingProduct = productRepository
                    .findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Cannot find ProductImage id"));
            ProductImage newProductImage = ProductImage.builder()
                    .product(exitstingProduct)
                    .imageUrl(productImageDTO.getImageUrl())
                    .build();
            //Ko cho insert quá 5 ảnh cho 1 sản phẩm
            int size = productImageRepository.findByProductId(productId).size();
            if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                throw new InvalidParamException("Number of images must be <= 5");
            }
            return productImageRepository.save(newProductImage);
    }
}
