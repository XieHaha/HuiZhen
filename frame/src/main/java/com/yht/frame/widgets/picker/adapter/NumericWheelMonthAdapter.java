package com.yht.frame.widgets.picker.adapter;

import com.yht.frame.widgets.picker.wheelview.adapter.WheelAdapter;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelMonthAdapter implements WheelAdapter {
    private int minValue;
    private int maxValue;

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelMonthAdapter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
            int value = minValue + index;
            return value > 12 ? value - 12 : value;
        }
        return 0;
    }

    @Override
    public int getItemsCount() {
        return 12;
    }

    @Override
    public int indexOf(Object o) {
        try {
            return (int)o - minValue;
        }
        catch (Exception e) {
            return -1;
        }
    }
}
