import { registerCharacter, existsCharacter, getCharacteristics, wasAttacked, CharacterRegisterRecordBody, ClassTypeEnum } from './CharacterApi';
import api from './api';
import { MonsterTypeEnum } from './MonsterApi';

jest.mock('./api');

describe('CharacterApi', () => {
    describe('registerCharacter', () => {
        it('registers a character successfully', async () => {
            const character: CharacterRegisterRecordBody = { username: 'testUser', classType: ClassTypeEnum.WARRIOR };
            const response = { data: { id: 1, username: 'testUser', classType: ClassTypeEnum.WARRIOR, inventory: {}, health: 100 } };
            (api.post as jest.Mock).mockResolvedValue(response);

            const result = await registerCharacter(character);

            expect(result).toEqual(response.data);
            expect(api.post).toHaveBeenCalledWith('/character', character);
        });

        it('fails to register a character with missing username', async () => {
            const character: CharacterRegisterRecordBody = { username: '', classType: ClassTypeEnum.WARRIOR };
            (api.post as jest.Mock).mockRejectedValue(new Error('Username is required'));

            await expect(registerCharacter(character))
                .rejects
                .toThrow('Username is required');
        });
    });

    describe('existsCharacter', () => {
        it('returns true if character exists', async () => {
            (api.get as jest.Mock).mockResolvedValue({ data: true });

            const result = await existsCharacter('testUser');

            expect(result).toBe(true);
            expect(api.get).toHaveBeenCalledWith('/character/testUser/exists');
        });

        it('returns false if character does not exist', async () => {
            (api.get as jest.Mock).mockResolvedValue({ data: false });

            const result = await existsCharacter('nonExistentUser');

            expect(result).toBe(false);
            expect(api.get).toHaveBeenCalledWith('/character/nonExistentUser/exists');
        });
    });

    describe('getCharacteristics', () => {
        it('retrieves characteristics successfully', async () => {
            const response = { data: { id: 1, strength: 10, intelligence: 15, luck: 5 } };
            (api.get as jest.Mock).mockResolvedValue(response);

            const result = await getCharacteristics('testUser');

            expect(result).toEqual(response.data);
            expect(api.get).toHaveBeenCalledWith('/character/testUser/characteristics');
        });

        it('fails to retrieve characteristics for non-existent user', async () => {
            (api.get as jest.Mock).mockRejectedValue(new Error('User not found'));

            await expect(getCharacteristics('nonExistentUser'))
                .rejects
                .toThrow('User not found');
        });
    });

    describe('wasAttacked', () => {
        it('updates character after being attacked', async () => {
            const wasAttackedBody = { username: 'testUser', monsterType: MonsterTypeEnum.DEMON };
            const response = { data: { id: 1, username: 'testUser', classType: ClassTypeEnum.WARRIOR, inventory: {}, health: 50 } };
            (api.put as jest.Mock).mockResolvedValue(response);

            const result = await wasAttacked(wasAttackedBody);

            expect(result).toEqual(response.data);
            expect(api.put).toHaveBeenCalledWith('/character/testUser/attacked/DEMON');
        });
    });
});