import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import {
    saveNewMonster,
    wasAttacked as monsterWasAttacked,
    MonsterTypeEnum,
    MonsterResponse,
} from "./api/MonsterApi.ts";
import {
    getCharacteristics,
    wasAttacked as characterWasAttacked,
    CharacteristicsResponse,
} from "./api/CharacterApi.ts";

interface Joueur {
    username: string;
    classType: string;
    caracteristiques: CharacteristicsResponse;
    health: number;
}

const Game: React.FC = () => {
    const location = useLocation();
    const joueur = location.state?.personnage as Joueur | null;

    const [monster, setMonster] = useState<MonsterResponse | null>(null);
    const [message, setMessage] = useState<string>("Bienvenue dans le jeu !");
    const [combatEnCours, setCombatEnCours] = useState<boolean>(false);
    const [vieJoueur, setVieJoueur] = useState<number>(joueur?.health || 100);

    // Fonction pour récupérer un monstre depuis le backend
    const recupererMonster = async () => {
        try {
            const response = await saveNewMonster(MonsterTypeEnum.RAT);
            setMonster(response);
            setMessage(`Un RAT apparaît ! Préparez-vous au combat.`);
            setCombatEnCours(true);
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
        } catch (error) {
            setMessage("Impossible de récupérer un monstre. Réessayez plus tard.");
        }
    };

    // Fonction pour gérer l'attaque du joueur
    const attaquer = async () => {
        if (joueur && monster) {
            try {
                const playerResponse = await characterWasAttacked({
                    username: joueur.username,
                    monsterType: monster.type,
                });

                if (vieJoueur === playerResponse?.health) {
                    setMessage("Vous avez eu de la chance ! Vous avez évité l'attaque du monstre."); // TODO: Fix message
                }

                setVieJoueur(playerResponse?.health || 0);

                const monsterResponse = await monsterWasAttacked(monster.id, joueur.username);

                if (monsterResponse) {
                    setMonster(monsterResponse);
                }

                if ((playerResponse?.health || 0) <= 0) {
                    setMessage("Vous avez été vaincu. Fin de la partie.");
                    setCombatEnCours(false);
                } else if ((monsterResponse?.health || 0) <= 0) {
                    await gagnerCombat();
                } else {
                    if (playerResponse) {
                        setMessage(
                            `Vous attaquez un ${MonsterTypeEnum[monster.type]} ! Vie du joueur : ${playerResponse.health}, vie du monstre : ${monsterResponse?.health}.`
                        );
                    }
                }
                // eslint-disable-next-line @typescript-eslint/no-unused-vars
            } catch (error) {
                setMessage("Une erreur est survenue lors du combat.");
            }
        }
    };

    // Fonction pour gérer la victoire et récupérer les objets
    const gagnerCombat = async () => {
        if (monster) {
            try {
                const objetsRecuperes = monster.droppedObject;
                setMessage(
                    `Vous avez vaincu un ${MonsterTypeEnum[monster.type]} et obtenu l'objet : ${objetsRecuperes.name}.`
                );

                setCombatEnCours(false);
                await recupererMonster();
                // eslint-disable-next-line @typescript-eslint/no-unused-vars
            } catch (error) {
                setMessage("Une erreur est survenue lors de la récupération des objets.");
            }
        }
    };

    useEffect(() => {
        if (!joueur || !joueur.username) {
            setMessage("Impossible de récupérer les informations du joueur. Vérifiez votre connexion.");
            return;
        }

        const initGame = async () => {
            try {
                const response = await getCharacteristics(joueur.username);
                if (response === null) throw new Error("Impossible de récupérer les caractéristiques du joueur.");

                joueur.caracteristiques = response;
                await recupererMonster();

                // eslint-disable-next-line @typescript-eslint/no-unused-vars
            } catch (error) {
                setMessage("Impossible de démarrer le jeu. Vérifiez votre connexion.");
            }
        };

        initGame();
    }, [joueur]);

    if (!joueur || !joueur.username) {
        return <div>Chargement des informations du joueur ou données manquantes...</div>;
    }

    return (
        <div id="plateauDeJeu" style={{ padding: "20px" }}>
            <h2>Combat contre un monstre</h2>

            {monster && combatEnCours ? (
                <>
                    <div>
                        <h3>Votre Personnage</h3>
                        <p>Nom : {joueur.username}</p>
                        <p>Classe : {joueur.classType}</p>
                        <p>Vie : {vieJoueur}</p>
                        <p>Force : {joueur.caracteristiques.strength}</p>
                        <p>Intelligence : {joueur.caracteristiques.intelligence}</p>
                        <p>Chance : {joueur.caracteristiques.luck}</p>
                    </div>

                    <div>
                        <h3>Le Monstre</h3>
                        <p>Type : {MonsterTypeEnum[monster.type]}</p>
                        <p>Vie : {monster.health}</p>
                        <p>Objet récupérable : {monster.droppedObject.name}</p>
                    </div>

                    <div>
                        <button onClick={attaquer} disabled={!combatEnCours}>
                            Attaquer
                        </button>
                    </div>

                    <div>
                        <p>{message}</p>
                        {monster.health <= 0 && (
                            <button onClick={recupererMonster}>Prochain Combat</button>
                        )}
                    </div>
                </>
            ) : (
                <p>{message}</p>
            )}
        </div>
    );
};

export default Game;
