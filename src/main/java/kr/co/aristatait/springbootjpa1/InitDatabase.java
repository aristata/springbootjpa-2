package kr.co.aristatait.springbootjpa1;

import kr.co.aristatait.springbootjpa1.service.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDatabase {

    private final InitService initService;

    //    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }
}
