package com.company.inventory.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.company.inventory.modelo.Categoria;

public class CategoriaExcelExportar {
	//referencia al libro
	private XSSFWorkbook workbook;
	//las hojas
	private XSSFSheet sheet;
	private List<Categoria> categoria;
	
	
	public CategoriaExcelExportar(List<Categoria> categorias) {
		this.categoria=categorias;		
		workbook =  new XSSFWorkbook();
	}
	
	//escribira en al cabecera del archivo excel
	private void writeHeaderLine() {
		sheet= workbook.createSheet("Resultado");
		Row row= sheet.createRow(0);//columna 0 del excel se crea la fila
		CellStyle style= workbook.createCellStyle(); //crear stylo a la celda
		
		XSSFFont font= workbook.createFont(); //crear la fuente que tendra el libro
		font.setBold(true);
		font.setFontHeight(16);//alto de la cabecera
		style.setFont(font); //asigno el stilo a la cabecera
		
		crearCell(row,0,"ID",style);
		crearCell(row,1,"NOMBRE",style);
		crearCell(row,2,"DESCRIPCION",style);
	}
	
	//CREAR LAS CELDAS
	private void crearCell(Row row, int columCount,Object value,CellStyle style) {
		sheet.autoSizeColumn(columCount);
		Cell cell = row.createCell(columCount);
		
		//si el value es un entero colocarlo
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		}else if(value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		}else {
			cell.setCellValue((String) value);
		}
		//se le asigna el style que se manda
		cell.setCellStyle(style);
	}
	
	
	//ESCRIBIR LOS DATOS
	private void writeDataLines() {
		int rowCount = 1; //contador de filas
		CellStyle style = workbook.createCellStyle();
		
		XSSFFont font =workbook.createFont();
		font.setFontHeight(14);//alto
		style.setFont(font);
		
		for(Categoria re: categoria) {
			Row row = sheet.createRow(rowCount++);
			int columCount =0; //contador de columnas
			
			crearCell(row,columCount++,String.valueOf(re.getId()),style);
			crearCell(row,columCount++,re.getNombre(),style);
			crearCell(row,columCount++,re.getDescripcion(),style);
			
		}
		
	}
	
	
	//exportar el excel con ecepcion de entrada y salida(trabajar con archivos)
	public void exportar(HttpServletResponse response)throws IOException{
		
		writeHeaderLine();//colcoar la cabecera
		writeDataLines();//scribir la data
		
		ServletOutputStream servletOS=response.getOutputStream();
		workbook.write(servletOS);//escribir con el obj SOS
		workbook.close();//cerrar libro excel
		servletOS.close();//cerrar el obj tipo OutputStream
		
	}

}
