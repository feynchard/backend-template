package com.example.demo.web;

import com.example.demo.domain.Goods;
import com.example.demo.domain.SystemUser;
import com.example.demo.pojo.http.GoodsVO;
import com.example.demo.pojo.http.UserVO;
import com.example.demo.repository.GoodsRepository;
import com.example.demo.service.SystemUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/priv/goods")
//@SecurityRequirement(name = "Bearer Authentication")
//@Tag(name = "Goods", description = "The Goods API. Contains all the operations that can be performed on a goods.")
public class GoodsApi {

    private GoodsRepository goodsRepository;

    private SystemUserService systemUserService;

    public GoodsApi(GoodsRepository goodsRepository, SystemUserService systemUserService) {
        this.goodsRepository = goodsRepository;
        this.systemUserService = systemUserService;
    }


    @GetMapping("/")
    public ResponseEntity<List<GoodsVO>> getAllGoods() {
        List<Goods> all = goodsRepository.findAll();
        List<GoodsVO> vos = all.stream().map(goods -> GoodsVO.builder()
                        .id(goods.getId())
                        .name(goods.getName())
                        .build())
                .toList();
        return ResponseEntity.ok(vos);
    }

    @GetMapping("{id}")
    public ResponseEntity<GoodsVO> getGoodsById(@PathVariable UUID id) {
        Optional<Goods> optional = goodsRepository.findById(id);
        Goods goods = optional.get();
        return ResponseEntity.ok(GoodsVO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .build());
    }

    @PostMapping("/")
    public ResponseEntity<GoodsVO> createGoods(
        @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
        @RequestBody GoodsVO vo) {
        String token = authorization.split(" ")[1];

        Optional<SystemUser> systemUserOptional = systemUserService.getSystemUserByToken(token);

        Goods goods = Goods.builder()
                .id(UUID.randomUUID())
                .name(vo.getName())
                .build();

        goods.setCreateInfo(systemUserOptional.get().getId());

        goodsRepository.save(goods);

        return ResponseEntity.ok(GoodsVO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .build());
    }

    @PutMapping("{id}")
    public ResponseEntity<GoodsVO> updateGoodsById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authentication,
            @PathVariable UUID id, @RequestBody GoodsVO vo) {
        String token = authentication.split(" ")[1];

        Optional<SystemUser> systemUserOptional = systemUserService.getSystemUserByToken(token);

        Optional<Goods> optional = goodsRepository.findById(id);
        Goods goods = optional.get();
        goods.setName(vo.getName());

        goods.setUpdateInfo(systemUserOptional.get().getId());

        goodsRepository.save(goods);

        return ResponseEntity.ok(GoodsVO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteGoodsById(@PathVariable UUID id) {
        goodsRepository.deleteById(id);
        return ResponseEntity.ok("success");
    }

}