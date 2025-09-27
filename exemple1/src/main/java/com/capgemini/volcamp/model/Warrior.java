package com.capgemini.volcamp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Représente un guerrier généré par le LLM.
 * Exemple JSON attendu:
 * {
 *   "name": "Borg le Titan",
 *   "strength": 95,
 *   "agility": 42,
 *   "weapons": ["Hache à deux mains", "Dague"]
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warrior {
    private String name;
    private int strength;
    private int agility;
    private List<String> weapons;
}

