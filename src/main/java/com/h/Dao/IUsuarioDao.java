package com.h.Dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;


import com.h.entity.Usuario;

import jakarta.persistence.Tuple;

@Repository
public interface IUsuarioDao extends JpaRepository<Usuario,Integer> {
 ///Para buscar un registro en particular mediante el correo
   Optional<Usuario> findByCorreo(String correo);
   
   
 //Metodo para llamar todos los usuarios
 	List<Usuario> findByTipo(String tipo);
 	
 //Consulta personalizada que me trae los usuarios con las compras que ha hecho y el total de dinero que ha gastado
 	@Query(value = """
 	   SELECT u.id AS id, 
       u.nombre AS nombre, 
       u.cedula AS cedula, 
       u.apellido AS apellido, 
       u.correo AS correo, 
       u.telefono AS telefono,
       u.tipo AS tipo,
       COUNT(o.usuario_id) AS numeroCompras,
 	   COALESCE(SUM(o.total), 0) AS totalCompras
 	   FROM usuarios u
 	   LEFT JOIN ordenes o ON u.id = o.usuario_id
 	   GROUP BY u.id, u.nombre
 	   ORDER BY totalCompras desc
 		    """, nativeQuery = true)
 		Page<Tuple> findUsuario(Pageable pageable);
 	
 	
 	
 	//Buscar un usuario mediante su cedula con las compras que ha hecho y el total de dinero que ha gastado
 	@Query(value = """
 		    SELECT u.id AS id, 
 		           u.nombre AS nombre, 
 		           u.cedula AS cedula, 
 		           u.apellido AS apellido, 
 		           u.correo AS correo, 
 		           u.telefono AS telefono,
 		           u.tipo AS tipo,
 		           COUNT(o.usuario_id) AS numeroCompras,
 		           COALESCE(SUM(o.total), 0) AS totalCompras
 		      FROM usuarios u
 		 LEFT JOIN ordenes o ON u.id = o.usuario_id
 		     WHERE u.cedula = :cedula
 		  GROUP BY u.id, u.nombre
 		  ORDER BY totalCompras DESC
 		""", nativeQuery = true)
 		Page<Tuple> findUsuarioCedula(@Param("cedula") String cedulaUsuario, Pageable pageable);


 	
 	
 	
 
}