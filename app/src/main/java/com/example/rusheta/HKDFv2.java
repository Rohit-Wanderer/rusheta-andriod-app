package com.example.rusheta;

public class HKDFv2 extends HKDF {
    @Override
    protected int getIterationStartOffset() {
        return 0;
    }
}
