package ge.chalauri.fantasy.controllers;

import java.util.List;

import ge.chalauri.fantasy.aspect.annotations.HasRole;
import ge.chalauri.fantasy.model.Transfer;
import ge.chalauri.fantasy.model.request.TransferSearch;
import ge.chalauri.fantasy.security.utils.Role;
import ge.chalauri.fantasy.services.TransferService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/transfer")
@RestController
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/put")
    public Transfer putOnTransfer(@RequestBody Transfer transfer) {

        return transferService.putOnTransfer(transfer, false);
    }

    @PostMapping("/create")
    @HasRole(Role.ADMIN)
    public Transfer putOnTransferByAdmin(@RequestBody Transfer transfer) {

        return transferService.putOnTransfer(transfer, true);
    }

    @PostMapping("/list")
    public List<Transfer> search(@RequestBody TransferSearch transferSearch) {

        return transferService.search(transferSearch);
    }

    @GetMapping("/buy/{transferId}")
    public Transfer buy(@PathVariable("transferId") Integer transferId) {

        return transferService.buy(transferId);
    }
}
