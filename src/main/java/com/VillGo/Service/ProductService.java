package com.VillGo.Service;

import com.VillGo.DTO.ProductActionDTO;
import com.VillGo.DTO.ProductRequestDTO;
import com.VillGo.DTO.Responce.DataResponse;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.Responce.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<byte[]> downloadProductsPdf();
    ResponseEntity<MessageResponse> create(ProductRequestDTO dto);
    ResponseEntity<DataResponse<List<ProductResponse>>> getAll();
    ResponseEntity<DataResponse<ProductResponse>> getOne(Integer id);
    ResponseEntity<MessageResponse> delete(Integer id);
    ResponseEntity<MessageResponse> restore(Integer id);
    ResponseEntity<MessageResponse> changeProductStatus(Integer id, ProductActionDTO dto);
    ResponseEntity<MessageResponse> update(Integer id, ProductRequestDTO dto);
    ResponseEntity<DataResponse<List<ProductResponse>>> getAllActive();
    ResponseEntity<MessageResponse> getMyProducts();
    ResponseEntity<DataResponse<List<ProductResponse>>> getAllDeleted();

}

