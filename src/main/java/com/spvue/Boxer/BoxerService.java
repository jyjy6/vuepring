package com.spvue.Boxer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class BoxerService {
    private final BoxerRepository boxerRepository;

    public void saveBoxer(Boxer boxer){
        boxerRepository.save(boxer);

    }

    public List<Boxer> getAllBoxerByDivision(String weightClass) {
        List<Boxer> boxerList = boxerRepository.getAllBoxerByDivision(weightClass);
        return boxerList;
    }



}
