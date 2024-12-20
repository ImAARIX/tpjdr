import '@testing-library/jest-dom';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import Game from './Game';
import { saveNewMonster, monsterWasAttacked } from './api/MonsterApi';
import { getCharacteristics, characterWasAttacked } from './api/CharacterApi';

// Mock des modules
jest.mock('./api/MonsterApi');
jest.mock('./api/CharacterApi');

// Configuration des mocks
const mockJoueur = {
    username: "testUser",
    classType: "Guerrier",
    health: 100,
    caracteristiques: {
        strength: 10,
        intelligence: 8,
        luck: 5
    }
};

const mockMonster = {
    id: 1,
    type: 0, // RAT
    health: 50,
    droppedObject: {
        name: "Potion"
    }
};

describe('Game Component', () => {
    beforeEach(() => {
        // Reset des mocks avant chaque test
        jest.clearAllMocks();
        
        // Configuration des mocks par défaut
        (saveNewMonster as jest.Mock).mockResolvedValue(mockMonster);
        (getCharacteristics as jest.Mock).mockResolvedValue(mockJoueur.caracteristiques);
    });

    const renderGameComponent = () => {
        return render(
            <MemoryRouter initialEntries={[{ pathname: '/game', state: { personnage: mockJoueur } }]}>
                <Routes>
                    <Route path="/game" element={<Game />} />
                </Routes>
            </MemoryRouter>
        );
    };

    test('affiche correctement les informations du joueur et du monstre', async () => {
        renderGameComponent();

        await waitFor(() => {
            expect(screen.getByText(`Nom : ${mockJoueur.username}`)).toBeInTheDocument();
            expect(screen.getByText(`Classe : ${mockJoueur.classType}`)).toBeInTheDocument();
            expect(screen.getByText(`Vie : ${mockJoueur.health}`)).toBeInTheDocument();
            expect(screen.getByText(`Force : ${mockJoueur.caracteristiques.strength}`)).toBeInTheDocument();
        });
    });

    test('gère correctement une attaque réussie', async () => {
        // Configurer les mocks avec des promesses résolues immédiatement
        const mockCharacterResponse = { 
            health: 90,
            username: "testUser",
            classType: "Guerrier"
        };
        const mockMonsterResponse = { 
            ...mockMonster, 
            health: 30
        };
        
        (characterWasAttacked as jest.Mock).mockImplementation(() => Promise.resolve(mockCharacterResponse));
        (monsterWasAttacked as jest.Mock).mockImplementation(() => Promise.resolve(mockMonsterResponse));

        renderGameComponent();
        
        // Attendre que le composant soit complètement chargé
        await screen.findByText('Combat contre un monstre');
        
        const attackButton = await screen.findByText('Attaquer');
        await fireEvent.click(attackButton);

        // Attendre que les appels d'API soient effectués
        await waitFor(() => {
            expect(characterWasAttacked).toHaveBeenCalled();
        });
        await waitFor(() => {
            expect(monsterWasAttacked).toHaveBeenCalled();
        });
        
        // Vérifier l'état final
        expect(await screen.findByText('Vie : 90')).toBeInTheDocument();
    });

    test('gère correctement la mort du monstre', async () => {
        const mockCharacterResponse = { 
            health: 90,
            username: "testUser",
            classType: "Guerrier"
        };
        const mockMonsterResponse = { 
            ...mockMonster, 
            health: 0
        };
        
        (characterWasAttacked as jest.Mock).mockImplementation(() => Promise.resolve(mockCharacterResponse));
        (monsterWasAttacked as jest.Mock).mockImplementation(() => Promise.resolve(mockMonsterResponse));

        renderGameComponent();
        
        await screen.findByText('Combat contre un monstre');
        
        const attackButton = await screen.findByText('Attaquer');
        await fireEvent.click(attackButton);

        // Utiliser findByText au lieu de getByText pour attendre le message
        await screen.findByText(/Le monstre a été vaincu/i);
    });

    test('gère correctement la mort du joueur', async () => {
        (characterWasAttacked as jest.Mock).mockResolvedValue({ 
            health: 0,
            username: "testUser",
            classType: "Guerrier"
        });
        (monsterWasAttacked as jest.Mock).mockResolvedValue({ 
            ...mockMonster,
            id: 1,
            type: 0,
            droppedObject: { name: "Potion" }
        });

        renderGameComponent();

        const attackButton = await screen.findByText('Attaquer');
        await fireEvent.click(attackButton);

        await screen.findByText("Vous avez été vaincu. Fin de la partie.");
    });

    test('gère les erreurs de chargement du monstre', async () => {
        (saveNewMonster as jest.Mock).mockRejectedValue(new Error('Erreur API'));

        renderGameComponent();

        await waitFor(() => {
            expect(screen.getByText(/impossible de récupérer un monstre/i)).toBeInTheDocument();
        });
    });
});