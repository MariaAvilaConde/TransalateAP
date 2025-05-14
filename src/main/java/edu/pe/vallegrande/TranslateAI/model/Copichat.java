package edu.pe.vallegrande.TranslateAI.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("copichat")
public class Copichat  {

    @Id
    private Long id;
    
    private String pregunta;
    
    private String respuesta;
    
    private LocalDate fecha;
    
    private char status;
}
