package org.flysky.coder.service.impl;

import org.flysky.coder.entity.Sector;
import org.flysky.coder.mapper.SectorMapper;
import org.flysky.coder.service.ISectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService implements ISectorService{
    @Autowired
    private SectorMapper sectorMapper;

    @Override
    public Integer createSector(String sectorName, Integer type) {
        if(sectorName==null){
            return 0;
        }

        if(sectorMapper.getSectorByName_COUNT(sectorName)>0){
            return 2;
        }

        Sector sector=new Sector();
        sector.setName(sectorName);
        sector.setType(type);
        sectorMapper.insert(sector);
        return 1;
    }

    @Override
    public Integer deleteSector(Integer sectorId) {
        sectorMapper.deleteByPrimaryKey(sectorId);
        return 1;
    }

    @Override
    public Integer updateSector(Integer sectorId,String sectorName) {
        Sector sector=sectorMapper.selectByPrimaryKey(sectorId);
        if(sector==null){
           return 0;
        }
        sector.setName(sectorName);
        sectorMapper.updateByPrimaryKey(sector);
        return 1;
    }

    @Override
    public List<Sector> listAllSectors() {
        return sectorMapper.selectAll();
    }
}
