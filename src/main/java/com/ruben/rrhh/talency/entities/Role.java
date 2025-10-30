package com.ruben.rrhh.talency.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // "COMPANY_ADMIN", "HR_MANAGER", "EMPLOYEE"


    /*
    | Rol                  | Permisos                                                                   | Descripción                                                   |
| -------------------- | -------------------------------------------------------------------------- | ------------------------------------------------------------- |
| 🟣 **SUPER_ADMIN**   | Gestiona todas las empresas.                                          | Puedes ver empresas, activar/desactivar cuentas, planes, etc. |
| 🔵 **COMPANY_ADMIN** | Crea usuarios de RRHH y gestiona toda su empresa.                          | Es el “cliente principal”.                                    |
| 🟢 **HR_MANAGER**    | Gestiona empleados, sin acceso a suscripciones o configuración de empresa. |                                                               |
| 🟠 **EMPLOYEE**      | Solo ve su perfil o nómina (opcional).                                     |                                                               |

    */
}
