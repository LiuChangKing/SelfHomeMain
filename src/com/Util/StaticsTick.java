package com.Util;


public class StaticsTick
{
  public String name;
  public int tile;
  public int chunk;
  
  public StaticsTick(String name, int tile, int chunk, int entity, int drop, double tps) {
    this.name = name;
    this.tile = tile;
    this.chunk = chunk;
    this.entity = entity;
    this.drop = drop;
    this.tps = tps;
  }
  
  public int entity;
  public int drop;
  public double tps;
}


