package com.kodat.of.halleyecommerce.discount;

import com.kodat.of.halleyecommerce.dto.discount.DiscountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Discounts")
@RestController
@RequiredArgsConstructor
@RequestMapping("/discounts")
public class DiscountController {
    private final DiscountService discountService;


    @Operation(summary = "Create discount to products", description = "Creating discounts to preferred products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Discount successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")

    })
    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<DiscountDto> createDiscount(
            @Valid @RequestBody DiscountDto discountDto,
            Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.createDiscount(discountDto, connectedUser));
    }

    @Operation(summary = "Update discount to products", description = "Update discounts to preferred products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Discount successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request")

    })
    @Secured("ADMIN")
    @PutMapping("/{discountId}")
    public ResponseEntity<DiscountDto> updateDiscountById(
            @PathVariable Long discountId,
            @Valid @RequestBody DiscountDto discountDto,
            Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.updateDiscountById(discountId, discountDto, connectedUser));
    }

    @Operation(summary = "Delete discount to products", description = "Deleting discounts to preferred products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Discount successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid request")

    })
    @Secured("ADMIN")
    @DeleteMapping("/{discountId}")
    public ResponseEntity<Void> deleteDiscount(
            @PathVariable Long discountId,
            Authentication connectedUser
    ) {
        discountService.deleteDiscount(discountId, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get discount to products", description = "Getting discounts to preferred products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount successfully fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid request")

    })
    @Secured("ADMIN")
    @GetMapping("/{discountId}")
    public ResponseEntity<DiscountDto> getDiscountById(
            @PathVariable Long discountId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(discountService.getDiscountById(discountId, connectedUser));
    }

    @Operation(summary = "Get all discounts", description = "Get all discounts to preferred products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount successfully fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid request")

    })
    @Secured("ADMIN")
    @GetMapping
    public ResponseEntity<List<DiscountDto>> getAllDiscounts(
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(discountService.getAllDiscounts(connectedUser));
    }


}
