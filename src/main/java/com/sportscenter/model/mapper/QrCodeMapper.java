package com.sportscenter.model.mapper;

import com.sportscenter.model.entity.QrCodeEntity;
import com.sportscenter.model.view.QrCodeViewModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BookingMapper.class)
public interface QrCodeMapper {

    QrCodeViewModel qrEntityToViewModel(QrCodeEntity qrCodeEntity);
}
