package org.flysky.coder.controller;

import org.flysky.coder.controller.wrapper.ResultWrapper;
import org.flysky.coder.service.ISectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SectorController {
    @Autowired
    private ISectorService sectorService;

    @RequestMapping("/sector/create/{type}/{name}")
    public ResultWrapper createSector(@PathVariable String name,@PathVariable int type){
        Integer result=sectorService.createSector(name,type);
        return new ResultWrapper(result);
    }

    @RequestMapping("/sector/delete/{sectorId}")
    public ResultWrapper deleteSector(@PathVariable int sectorId){
        Integer result=sectorService.deleteSector(sectorId);
        return new ResultWrapper(result);
    }

    @RequestMapping("/sector/update/{sectorId}/{newName}")
    public ResultWrapper updateSector(@PathVariable int sectorId,@PathVariable String newName) {
        Integer result = sectorService.updateSector(sectorId, newName);
        return new ResultWrapper(result);
    }

}
