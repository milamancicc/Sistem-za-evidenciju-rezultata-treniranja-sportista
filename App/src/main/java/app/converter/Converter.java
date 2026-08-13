/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.converter;


/**
 *
 * @author PC
 */
public interface Converter<Entity, Dto> {
    public Entity toEntity(Dto dto);
    public Dto toDto(Entity entity);
}
