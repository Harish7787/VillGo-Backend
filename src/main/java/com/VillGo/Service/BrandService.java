package com.VillGo.Service;

import com.VillGo.DTO.ActionChangeRequest;
import com.VillGo.DTO.BrandRequestDTO;
import com.VillGo.DTO.UpdateDTO.BrandUpdateDTO;
import com.VillGo.DTO.Responce.BrandResponse;
import com.VillGo.DTO.Responce.DataResponse;
import com.VillGo.DTO.Responce.ListResponse;
import com.VillGo.DTO.Responce.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrandService {

    ResponseEntity<MessageResponse> create(BrandRequestDTO dto);
    ResponseEntity<MessageResponse> update(Integer id, BrandUpdateDTO dto);
    ResponseEntity<ListResponse<BrandResponse>> getAll();
    ResponseEntity<DataResponse<BrandResponse>> getById(Integer id);
    MessageResponse<?> softDeleteBrand(Integer id);
    MessageResponse<?> restoreBrand(Integer id);
    ResponseEntity<MessageResponse> actionChange(Integer id , ActionChangeRequest request);
    List<BrandResponse> getAllDeletedBrands();
    List<BrandResponse> getAllActiveBrands();
}

