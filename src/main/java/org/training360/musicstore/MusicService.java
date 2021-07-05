package org.training360.musicstore;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@NoArgsConstructor
public class MusicService {
    private AtomicLong idGenerator = new AtomicLong();
    private List<Instrument> instruments = new ArrayList<>();

    @Autowired
    ModelMapper modelMapper;


    public InstrumentDTO addInstrument(CreateInstrumentCommand command) {
        Instrument instrument =
                new Instrument(
                        idGenerator.incrementAndGet(),
                        command.getBrand(),
                        command.getType(),
                        command.getPrice(),
                        LocalDate.now()
                );
        instruments.add(instrument);
        return modelMapper.map(instrument, InstrumentDTO.class);
    }

    public List<InstrumentDTO> getInstruments(Optional<String> brand, Optional<Integer> price) {

        return instruments.stream()
                .filter(i -> brand.isEmpty() || i.getBrand().equalsIgnoreCase(brand.get()))
                .filter(i -> price.isEmpty() || i.getPrice() == price.get())
                .map(i -> modelMapper.map(i, InstrumentDTO.class))
                .toList();
    }

    public InstrumentDTO findInstrumentById(Long id) {
        Instrument found = findById(id);
        return modelMapper.map(found,InstrumentDTO.class);
    }

    private Instrument findById(Long id) {
        return instruments.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Instrumen not foud with this id: " + id));
    }

    public void deleteAllInstruments() {
        idGenerator = new AtomicLong();
        instruments.clear();
    }

    public void deleteInstrumentById(Long id) {
        instruments.remove(findById(id));
    }

    public InstrumentDTO updatePriceById(Long id, UpdatePriceCommand command) {
        Instrument instrument = findById(id);
        if (instrument.getPrice() != command.getPrice()) {
            instrument.setPrice(command.getPrice());
            instrument.setPostDate(LocalDate.now());
        }
        return modelMapper.map(instrument, InstrumentDTO.class);
    }


}
