package org.flysky.coder.service;

import org.flysky.coder.entity.Sector;

import java.util.List;

public interface ISectorService {
    Integer createSector(String sectorName,Integer type);
    Integer deleteSector(Integer sectorId);
    Integer updateSector(Integer sectorId,String sectorName);
    List<Sector> listAllSectors();
}
