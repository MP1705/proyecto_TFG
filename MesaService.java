package com.daw.proyecto_v2.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.proyecto_v2.entity.Mesa;
import com.daw.proyecto_v2.repositories.MesaRepository;

@Service
public class MesaService {

      private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public List<Mesa> listarMesas() {
        return mesaRepository.findAll();
    }

    public Optional<Mesa> buscarPorId(int id) {
        return mesaRepository.findById(id);
    }

}
