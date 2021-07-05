package org.training360.musicstore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstrumentCommand {
    @NotBlank
    private String brand;

    @NonNull
    private InstrumentType type;

    @PositiveOrZero
    private Integer price;
}

//márkát, típust és árat várjuk, a dátumot az aznapi dátumra állítsuk be