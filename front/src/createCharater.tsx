import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
//import api from "./api/api.tsx";
import * as CharacterApi from "./api/CharacterApi.ts";
import {CharacterRegisterRecordBody} from "./api/CharacterApi.ts";

interface Caracteristiques {
    strength: number;
    intelligence: number;
    luck: number;
}

// Utilisation de l'énumération pour les classes
export enum ClassTypeEnum {
    WARRIOR = "WARRIOR",
    MAGE = "MAGE",
    POOR = "POOR"
}

// Mapping des classes et de leurs caractéristiques
const CHARACTER_CLASSES: { [key in ClassTypeEnum]: Caracteristiques } = {
    [ClassTypeEnum.WARRIOR]: { strength: 10, intelligence: 5, luck: 3 },
    [ClassTypeEnum.MAGE]: { strength: 3, intelligence: 10, luck: 4 },
    [ClassTypeEnum.POOR]: { strength: 2, intelligence: 2, luck: 15 },
};

const CreationPersonnage: React.FC = () => {
    const [nom, setNom] = useState<string>(""); // Nom du personnage
    const [classe, setClasse] = useState<ClassTypeEnum | "">(""); // Classe choisie
    const [caracteristiques, setCaracteristiques] = useState<Caracteristiques>({
        strength: 0,
        intelligence: 0,
        luck: 0,
    });

    const navigate = useNavigate();

    const handleClassChange = (selectedClass: string) => {
        if (selectedClass in ClassTypeEnum) {
            // Assurez-vous que selectedClass correspond à une valeur de ClassTypeEnum
            const classEnum = selectedClass as ClassTypeEnum;
            setClasse(classEnum); // Mise à jour de la classe sélectionnée
            setCaracteristiques(CHARACTER_CLASSES[classEnum]); // Mise à jour des caractéristiques selon la classe
        }
    };

    const handleSubmit = async () => {
        if (!nom || !classe) {
            alert("Veuillez entrer un nom et choisir une classe !");
            return;
        }

        const characterData: CharacterRegisterRecordBody = {
            username: nom,
            classType: classe,
        };

        try {
            // Appel à l'API pour créer le personnage
            const response = await CharacterApi.registerCharacter(characterData)
            console.log("Personnage créé :", response);
            navigate("/game", { state: { personnage: response } });
        } catch (error) {
            console.error("Erreur lors de la création du personnage :", error);
            alert("Une erreur est survenue. Veuillez réessayer.");
        }
    };

    return (
        <div style={{ padding: "20px", maxWidth: "500px", margin: "0 auto" }}>
            <h2>Création de Personnage</h2>
            <div>
                <label>Nom du personnage :</label>
                <input
                    type="text"
                    value={nom}
                    onChange={(e) => setNom(e.target.value)}
                    placeholder="Entrez le nom"
                    style={{ display: "block", margin: "10px 0", width: "100%" }}
                />
            </div>

            <div>
                <label>Choisissez une classe :</label>
                <select
                    value={classe}
                    onChange={(e) => handleClassChange(e.target.value)}
                    style={{ display: "block", margin: "10px 0", width: "100%" }}
                >
                    <option value="">-- Sélectionnez une classe --</option>
                    <option value={ClassTypeEnum.WARRIOR}>Warrior</option>
                    <option value={ClassTypeEnum.MAGE}>Mage</option>
                    <option value={ClassTypeEnum.POOR}>Poor</option>
                </select>
            </div>

            {classe && (
                <div id="divCaracteristiques" style={{ marginTop: "20px" }}>
                    <h3>Caractéristiques</h3>
                    <p id="caracteristiquesStrength">Strength : {caracteristiques.strength}</p>
                    <p id="caracteristiquesIntelligence">Intelligence : {caracteristiques.intelligence}</p>
                    <p id="caracteristiquesLuck">Luck : {caracteristiques.luck}</p>
                </div>
            )}

            <button
                onClick={handleSubmit}
                style={{
                    marginTop: "20px",
                    padding: "10px 20px",
                    backgroundColor: "blue",
                    color: "white",
                    border: "none",
                    cursor: "pointer",
                }}
            >
                Créer le personnage
            </button>
        </div>
    );
};

export default CreationPersonnage;
