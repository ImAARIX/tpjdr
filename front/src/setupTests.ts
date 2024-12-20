import '@testing-library/jest-dom';

// Mock window object
global.window = {
  alert: jest.fn(),
  location: {
    href: 'http://localhost',
    origin: 'http://localhost'
  }
} as any;

// Add TextEncoder/TextDecoder
global.TextEncoder = require('node:util').TextEncoder;
global.TextDecoder = require('node:util').TextDecoder;

// Mock document object
global.document = {
  ...global.document,
  createRange: () => ({
    setStart: () => {},
    setEnd: () => {},
    commonAncestorContainer: {
      nodeName: 'BODY',
      ownerDocument: document,
    },
  }),
} as any;