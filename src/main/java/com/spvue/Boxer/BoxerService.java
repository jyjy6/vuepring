package com.spvue.Boxer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@Service
public class BoxerService {
    private final BoxerRepository boxerRepository;

    public void saveBoxer(Boxer boxer){
        boxerRepository.save(boxer);

    }

}
